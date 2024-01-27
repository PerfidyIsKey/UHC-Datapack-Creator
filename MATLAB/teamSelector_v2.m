clear
close all

addpath("Functions", "Documents", "Data")

Players = struct;
load("DataS51.mat")

%% Input
% Enter the players that are participating (corresponding numbers with
% PlayerName variable in Players struct)
participantIndex = [1, 2, 17, 25, 44, 45, 48, 54, 56];

%%% Enter the names of new players
newPlayers = ["Dan_Fingerman", "marckstef", "neokneipies", "blacksnake29", "BuildingBard300"];
estimatedRank = [60, 20, 10, 20, 60];

%%% Algorithm settings
teamPlayer          = 2;                    % Number of players per team
rankLowerBound      = 5;                    % Maximum negative deviation of score median
rankUpperBound      = 10;                   % Maximum positive deviation of score mean
rankLowerTolerance	= rankLowerBound + 10;  % Maximum allowed negative deviation
rankUpperTolerance  = rankUpperBound + 15;  % Maximum allowed positive deviation
maxConnections      = 2;                    % Maximum number of times players have played together
scoreNoise          = 20;                   % Additional score noise to account for inaccuracies
plotResults         = true;                 % Visualize results in real time
verboseMode         = false;                % Allow messages

% Conversion
settings = struct("players", teamPlayer, "rank", struct("LB", rankLowerBound, ...
    "UB", rankUpperBound, "LT", rankLowerTolerance, "UT", rankUpperTolerance), ...
    "connections", maxConnections, "noise", scoreNoise);

newAmount           = length(newPlayers);           % Number of new players
totalAmount         = length(Players) + newAmount;  % Total number of players
participantAmount   = length(participantIndex);	    % Number of participants

%% Participation criteria
% Pull the players that are participating in this season
playerName	= strings(participantAmount + newAmount, 1);
ranks       = zeros(participantAmount + newAmount, 1);
for i = 1:participantAmount + newAmount
    if i <= participantAmount
        playerName(i) = Players(participantIndex(i)).PlayerName;
        if ~isnan(Players(participantIndex(i)).Rank)
            ranks(i) = Players(participantIndex(i)).Rank;
        else
            lastActive  = find(Players(participantIndex(i)).Participation == true, 1, "last");
            ranks(i)    = Players(participantIndex(i)).RankHistory(lastActive);
        end
        Players(participantIndex(i)).Experience = sum(Players(participantIndex(i)).Participation);
    else
        playerName(i)       = newPlayers(i - participantAmount);
        ranks(i)            = estimatedRank(i - participantAmount);
        participantIndex(i) = totalAmount - newAmount + i - participantAmount;
        Players(participantIndex(i)).Experience = 1;
    end
end

%% Set up Genetic Algorithm
players	= 1:length(ranks);      % Label players
scores	= ranks;                % Label ranks

teamSize        = settings.players;                 % Number of players per team
playersNumber	= length(players);                  % Number of players
teamNumber      = floor(playersNumber / teamSize);  % Number of teams

%%% Specify bounds
lb = ones(playersNumber, 1);                % Lower bound (team 1)
ub = teamNumber * ones(playersNumber, 1);   % Upper bound (last team)

%%% Specify Genetic Algorithm options
options = optimoptions("ga", "Display", "none",...
    "OutputFcn",  @(x,  y,  z) gaoutfun(x,  y,  z,  playerName));

%% Evaluate Genetic Algorithm
while true
    for i = 1:length(ranks)
        noise       = settings.noise*exp(-Players(participantIndex(i)).Experience)*randn;   % Add random noise scaled with experience
        scores(i)   = max(ranks(i) + noise,  0);
    end
    
    %%% Define functions
    fun = @(x) groupPlayers(x, scores, teamNumber, teamSize);       % Objective function
    nonlcon = @(x) constrainTeams(x, scores, teamNumber, teamSize, ...
        Players, participantIndex, PlayerConnectivity, settings);   % Constraints
    
    [x, fval, exitflag, output, population, popScores] = ga(fun, playersNumber, [], [], [], [], ...
        lb, ub, nonlcon, 1:playersNumber, options);
    
    %%% Retrieve constraints on exit
    [g, h] = constrainTeams(x, scores, teamNumber, teamSize, ...
        Players, participantIndex, PlayerConnectivity, settings);
    
    %%% Check which constraints are violated
    sizeBool    = isempty(find(g(1:2*teamNumber) > 0, 1));
    topBool     = isempty(find(g((1:teamNumber) + 2*teamNumber) > settings.rank.UT, 1));
    downBool	= isempty(find(g((1:teamNumber) + 3*teamNumber) > settings.rank.LT, 1));
    
    if ~sizeBool
        % Team size violated
        if verboseMode == true
            fprintf("Team sizes are not correct.\n")
        end
    end
    if ~topBool
        % Upper tolerance violated --> increase upper tolerance
        if verboseMode == true
            fprintf("Balance is too overpowered,  increasing tolerance.\n")
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
            fprintf("Balance is too underwhelming,  increasing tolerance.\n")
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
%%% Display results in command window
teamScore = getTeamScore(x, scores, teamNumber, teamSize, 2);   % Get team scores
meanScore = mean(scores);                                       % Mean of team scores

colVec = ["Yellow", "Blue", "Red", "Purple", "Green", "Pink", "Black", "Orange", "Gray",...
    "Aqua", "D.Red", "D.Blue", "D.Aqua", "D.Green", "D.Gray", "White"];   % Team colors
fprintf("The mean score of all players is %3.0f.\n", meanScore)

printName	= strings(playersNumber, 1); % Preallocation
pCount      = 0;                        % Initialization
for i = 1:teamNumber
    names = strings(1, settings.players);   % Preallocation
    count = 0;                              % Initialization
    for ii = 1:playersNumber
        if x(ii) == i	% Add player if they are in this team
            % Update counters
            count   = count + 1;
            pCount	= pCount + 1;
            
            names(count)   = convertCharsToStrings(playerName(ii)); % Add player name to display list
            if count == 1   % Update display operators
                oper = '%s';
            else
                oper((end + 1):(end + 4)) = ', %s';
            end
            printName(pCount)   = "team join Team" + num2str(i-1) + " " + playerName(ii);   % Add player name to command list
        end
    end

    % Convert scores to strings
    score       = num2str(teamScore(i), "%3.0f");
    devScore	= num2str(teamScore(i) - meanScore, "%+2.0f");
    printChar = "Team %7s: " + oper + " Team score: %s (%s)\n";
    names(names == "") = [];
    fprintf(printChar, [colVec(i), names, score, devScore])
end

%% Write to file
filename = fullfile("Documents", "random_teams.mcfunction");    % Define document name
fileID = fopen(filename, "w");                                  % Open document
fprintf(fileID, "%s \n", printName);                            % Rewrite document
fclose(fileID);                                                 % Close document