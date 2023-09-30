clear
close all

addpath('Functions','Documents','Data')

Players = struct;
load('DataS49.mat')

%% Input
% Enter the players that are participating (corresponding numbers with
% PlayerName variable in Players struct)
ParticipantIndex = [1, 2, 17, 18, 25, 28];

%%% Enter the names of new players
NewPlayers = ["ICEturbo"];
EstimatedRank = [21];

%%% Algorithm settings
teamPlayer          = 2;        % Number of players per team
rankLowerBound      = 5;        % Maximum negative deviation of score median
rankUpperBound      = 40;        % Maximum positive deviation of score mean
rankLowerTolerance	= rankLowerBound + 10;       % Maximum allowed negative deviation
rankUpperTolerance  = rankUpperBound + 15;       % Maximum allowed positive deviation
maxConnections      = 2;        % Maximum number of times players have played together
scoreNoise          = 60;       % Additional score noise to account for inaccuracies
plotResults         = true;    % Visualize results in real time
verboseMode         = false;    % Allow messages

% Conversion
settings = struct('players',teamPlayer,'rank',struct('LB',rankLowerBound,...
    'UB',rankUpperBound,'LT',rankLowerTolerance,'UT',rankUpperTolerance),...
    'connections',maxConnections,'noise',scoreNoise);

NoN = length(NewPlayers);           % Number of new players
NoP = length(Players) + NoN;        % Total number of players
NoPar = length(ParticipantIndex);	% Number of participants

%% Participation criteria
% Pull the players that are participating in this season
PlayerName	= cell(NoPar+NoN,1);
Ranks       = zeros(NoPar+NoN,1);
for i = 1:NoPar + NoN
    if i <= NoPar
        PlayerName{i} = Players(ParticipantIndex(i)).PlayerName;
        if ~isnan(Players(ParticipantIndex(i)).Rank)
            Ranks(i) = Players(ParticipantIndex(i)).Rank;
        else
            lastActive = find(Players(ParticipantIndex(i)).Participation==1,1,'last');
            Ranks(i) = Players(ParticipantIndex(i)).RankHistory(lastActive);
        end
        Players(ParticipantIndex(i)).Experience = sum(Players(ParticipantIndex(i)).Participation);
    else
        PlayerName{i} = NewPlayers{i - NoPar};
        Ranks(i) = EstimatedRank(i-NoPar);
        ParticipantIndex(i) = NoP - NoN + i - NoPar;
        Players(ParticipantIndex(i)).Experience = 1;
    end
end

%% Set up Genetic Algorithm
players	= 1:length(Ranks);      % Label players
scores	= Ranks;                % Label ranks

teamSize        = settings.players;             % Number of players per team
playersNumber	= length(players);              % Number of players
teamNumber      = floor(playersNumber/teamSize);	% Number of teams

%%% Specify bounds
lb = ones(playersNumber,1);             % Lower bound (team 1)
ub = teamNumber*ones(playersNumber,1);  % Upper bound (last team)

%%% Specify Genetic Algorithm options
options = optimoptions('ga','Display','none');

%% Evaluate Genetic Algorithm
while true
    for i = 1:length(Ranks)
        scores(i) = max(Ranks(i)+settings.noise/(Players(ParticipantIndex(i)).Experience)^2*randn,0);
    end
    
    %%% Define functions
    fun = @(x)groupPlayers(x,scores,teamNumber,teamSize,...
        Players,ParticipantIndex,settings); % Objective function
    nonlcon = @(x)constrainTeams(x,scores,teamNumber,teamSize,...
        Players,ParticipantIndex,PlayerConnectivity,settings);  % Constraints
    vfun = {    % Visualization
        @(options,state,flag)teamAssign(options,state,flag,PlayerName)
        @(options,state,flag)teamProgress(options,state,flag,scores,teamNumber,...
        teamSize,Players,ParticipantIndex,settings)
        };
    
    if plotResults == true
        options = optimoptions(options,'PlotFcn',vfun);
    end
    
    [x,fval,exitflag,output,population,popScores] = ga(fun,playersNumber,[],[],[],[],...
        lb,ub,nonlcon,1:playersNumber,options);
    
    %%% Retrieve constraints on exit
    [g,h] = constrainTeams(x,scores,teamNumber,teamSize,...
        Players,ParticipantIndex,PlayerConnectivity,settings);
    
    %%% Check which constraints are violated
    sizeBool    = isempty(find(g(1:2*teamNumber)>0,1));
    topBool     = isempty(find(g((1:teamNumber)+2*teamNumber)>settings.rank.UT,1));
    downBool	= isempty(find(g((1:teamNumber)+3*teamNumber)>settings.rank.LT,1));
    
    if ~sizeBool
        % Team size violated
        if verboseMode == true
            fprintf('Team sizes are not correct.\n')
        end
    end
    if ~topBool
        % Upper tolerance violated --> increase upper tolerance
        if verboseMode == true
            fprintf('Balance is too overpowered, increasing tolerance.\n')
        end
        if randi(2) == 1
            settings.rank.UB = settings.rank.UB + 1;
        else
            settings.rank.UT = settings.rank.UT + 2;
        end
    end
    if ~downBool
        % Lower tolerance violated --> decrease lower tolerance
        if verboseMode == true
            fprintf('Balance is too underwhelming, increasing tolerance.\n')
        end
        if randi(2) == 1
            settings.rank.LB = settings.rank.LB + 1;
        else
            settings.rank.LT = settings.rank.LT + 2;
        end
    end
    
    if sizeBool && topBool && downBool
        % Stop iterations when all necessary constraints are satisfied
        break
    end
end

%% Display results
%%% Plot results
if verboseMode == true
    X = categorical(PlayerName);
    X = reordercats(X,PlayerName);
    figure();
    bar(X,x)
    ax = gca;
    ax.XAxis.TickLabelInterpreter = 'none';
end

%%% Display results in command window
teamScore = getTeamScore(x,scores,teamNumber,teamSize,...
    Players,ParticipantIndex,settings,2);	% Get team scores
meanScore = mean(scores);                   % Mean of team scores

ColVec = ["Yellow";"Blue";"Red";"Purple";"Green";"Pink";"Black";"Orange";"Gray";...
    "Aqua";"D.Red";"D.Blue";"D.Aqua";"D.Green";"D.Gray";"White"];   % Team colors
fprintf('The mean score of all players is %3.0f.\n',meanScore)

printName	= strings(playersNumber,1); % Preallocation
pCount      = 0;                        % Initialization
for i = 1:teamNumber
    names = strings(1,settings.players);    % Preallocation
    count = 0;                              % Initialization
    for ii = 1:playersNumber
        if x(ii) == i	% Add player if they are in this team
            % Update counters
            count   = count + 1;
            pCount	= pCount + 1;
            
            names(count)   = convertCharsToStrings(PlayerName{ii}); % Add player name to display list
            if count == 1   % Update display operators
                oper = '%s';
            else
                oper((end+1):(end+4)) = ', %s';
            end
            joinName            = ['team join Team',num2str(i-1),' ',PlayerName{ii}];
            printName(pCount)	= convertCharsToStrings(joinName);  % Add player name to command list
        end
    end
    % Convert scores to strings
    score       = num2str(teamScore(i),'%3.0f');
    devScore	= num2str(teamScore(i) - meanScore,'%+2.0f');
    printChar = ['Team %7s: ',oper,' Team score: %s (%s)\n'];
    names(names=="") = [];
    fprintf(printChar,[ColVec{i},names,score,devScore])
end

%% Write to file
filename = fullfile('Documents','random_teams.mcfunction');   % Define document name
fileID = fopen(filename,'w');           % Open document
fprintf(fileID,'%s \n',printName);      % Rewrite document
fclose(fileID);                         % Close document