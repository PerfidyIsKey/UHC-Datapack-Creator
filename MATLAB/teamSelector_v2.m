clear
close all

addpath("Functions", "Documents", "Data")
addpath("..\Files\DIORITE")

% Specify file names
filePlayers      = "players.txt";
filePreAssigned  = "preAssignedTeams.txt";

% Load data
Players = struct;
load("DataS56.mat")

%% Input
% Import players
[participantIndex, name, rank] = importPlayers(filePlayers);

% Algorithm settings
teamPlayer          = 2;                    % Number of players per team
rankLowerBound      = 5;                    % Maximum negative deviation of score median
rankUpperBound      = 5;                    % Maximum positive deviation of score mean
rankLowerTolerance	= rankLowerBound + 5;   % Maximum allowed negative deviation
rankUpperTolerance  = rankUpperBound + 5;   % Maximum allowed positive deviation
maxConnections      = 2;                    % Maximum number of times players have played together
scoreNoise          = 20;                   % Additional score noise to account for inaccuracies
plotResults         = true;                 % Visualize results
verboseMode         = false;                % Allow messages

% Conversion
settings = struct("players", teamPlayer, "rank", struct("LB", rankLowerBound, ...
    "UB", rankUpperBound, "LT", rankLowerTolerance, "UT", rankUpperTolerance), ...
    "connections", maxConnections, "noise", scoreNoise);

playerDataNumber    = length(Players);              % Number of players in the data base
participantAmount   = length(participantIndex);	    % Number of returning participants
seasonNumber        = length(Seasons);              % Define number of seasons played

%% Participation criteria
% Pull the players that are participating in this season
playerName	= strings(participantAmount, 1);
ranks       = zeros(participantAmount, 1);
for i = 1:participantAmount
    if participantIndex(i) <= playerDataNumber
        playerName(i) = Players(participantIndex(i)).PlayerName;
        if ~isnan(Players(participantIndex(i)).Rank)
            ranks(i)    = Players(participantIndex(i)).Rank;
        else
            ranks(i)    = rank(i);
        end

    else
        playerName(i)       = name(i);
        ranks(i)            = rank(i);
    end
end

%% Season data
today = datetime("today");  % Create a date for today
for i = seasonNumber:-1:1
    % Extract season date
    lastEligible = Seasons(i).Date;

    % Find time between today and season
    timeBetween         = between(lastEligible, today);
    [timeYears, ~, ~]   = split(timeBetween, ["years", "months", "days"]);  % Convert to years

    % Break loop if season is no longer valid
    if timeYears >= 4
        lastEligibleSeason      = i + 1;                            % Store index
        participationIndexing   = lastEligibleSeason:seasonNumber;  % Create index for participation
        break
    end
end

%% Add noise to ranks
scores = zeros(size(participantIndex)); % Preallocation
for i = 1:participantAmount
    % Determine experience of player
    if participantIndex(i) <= playerDataNumber
        thisParticipation = Players(participantIndex(i)).Participation(participationIndexing);
        if any(thisParticipation == true)
            experience = numel(find(thisParticipation == true));    % Determine experience
        else
            experience = 0;                                         % Returning player
        end
    else
        experience = 0; % New players have no experience
    end

    noise       = settings.noise*exp(-experience)*randn;        % Determine random noise based on experience
    scores(i)   = max(ranks(i) + noise,  0);                    % Add noise to rank
end

% Create arrays for optimization
participationIndex  = participantIndex; % Indices
optimRank           = scores;           % Ranks
playerNames         = playerName;       % Player names


%% Create pre-assigned teams
% Import pre-assigned teams
preAssignedTeams    = importTeams(filePreAssigned);
preAssignedNumber   = size(preAssignedTeams, 1);    % Number of pre-assigned teams

% Define team sizes
exPlayers       = sum(~isnan(preAssignedTeams), "all"); % Number of excluded players
optimizeAmount	= participantAmount - exPlayers;        % Number of players to be optimized
teamSize        = settings.players;                     % Number of players per team
teamNumber      = floor(optimizeAmount / teamSize);     % Number of teams
totalTeams      = teamNumber + preAssignedNumber;       % Total number of teams

% Initialize final team composition
finalTeams = zeros(1, participantAmount);

% Add pre-assigned teams to final team composition
count = 1;  % Initialize counter
for i = 1:preAssignedNumber
    preTeamSize = size(preAssignedTeams, 2);  % Size of current pre-assigned team
    for ii = 1:preTeamSize
        if isnan(preAssignedTeams(i, ii))
            % Break loop if player is found
            break
        end
        % Define index of current player
        index = find(participantIndex == preAssignedTeams(i, ii));

        % Add team of player to final teams
        finalTeams(optimizeAmount + count) = teamNumber + i;

        % Update counter
        count = count + 1;
    end
end

% Remove pre-assigned teams from optimization pool
preAssignedTeams                            = preAssignedTeams';
preAssignedTeams                            = preAssignedTeams(:)';
preAssignedTeams(isnan(preAssignedTeams))   = [];
preAssignedScores                           = zeros(1, exPlayers);
preAssignedNames                            = strings(1, exPlayers);
for i = 1:exPlayers
    index = participationIndex == preAssignedTeams(i);  % Index player to be removed
    preAssignedScores(i)        = optimRank(index);     % Store score for pre-assigned players
    preAssignedNames(i)         = playerNames(index);   % Store name for pre-assigned players
    optimRank(index)            = [];                   % Remove rank
    playerNames(index)          = [];                   % Remove player name
    participationIndex(index)   = [];                   % Remove participation index
end

%% Set up Genetic Algorithm
% Specify bounds
lb = ones(optimizeAmount, 1);                % Lower bound (team 1)
ub = teamNumber * ones(optimizeAmount, 1);   % Upper bound (last team)

% Specify Genetic Algorithm options
options = optimoptions("ga", "Display", "none");

% Define functions
fun     = @(x) groupPlayers(x, optimRank, teamNumber, teamSize);   % Objective function
nonlcon = @(x) constrainTeams(x, optimRank, teamNumber, teamSize, ...
    Players, participationIndex, PlayerConnectivity, settings);   % Constraints

%% Evaluate Genetic Algorithm
while true
    % Create manual initial population
    [~, I] = sort(optimRank, "descend");   % Get player ranking order
    meanPlayerScore = mean(optimRank);     % Get mean player scores

    initialPopulation = zeros(1, optimizeAmount);    % Preallocation
    for assignedTeam = 1:teamNumber
        assignedPlayers         = zeros(1, teamPlayer); % Initialize
        assignedIndex           = [1, length(I)];       % Take best and worst player
        assignedPlayers(1:2)    = I(assignedIndex);     % Assign actual player indices
        I(assignedIndex)        = [];                   % Remove players from callback list

        % Add extra team mates to team in case of inbalance
        if teamNumber - assignedTeam >= mod(optimizeAmount, teamPlayer)
            extraTeamPlayers = 0;
        else
            extraTeamPlayers = 1;
        end

        for teamIndex = 3:(teamPlayer + extraTeamPlayers)
            % Find next best players
            teamScores                  = (sum(optimRank(assignedPlayers(1:teamIndex-1))) +...
                optimRank(I))/teamIndex;                                               % Find team scores with all possible team mates
            [~, addedPlayer]            = min(abs(teamScores - meanPlayerScore));   % Find best player to add
            assignedPlayers(teamIndex)  = I(addedPlayer);                           % Add player to list
            I(addedPlayer) = [];                                                    % Remove players from callback list
        end

        % Assign team to initial population
        initialPopulation(assignedPlayers) = assignedTeam;
    end

    % Add initial population option
    options = optimoptions(options, "InitialPopulationMatrix", initialPopulation);

    % Genetic Algorithm
    [finalPopulation, fval, exitflag, output, population, popScores] = ga(fun, optimizeAmount, [], [], [], [], ...
        lb, ub, nonlcon, 1:optimizeAmount, options);
    
    % Retrieve constraints on exit
    [g, h] = constrainTeams(finalPopulation, optimRank, teamNumber, teamSize, ...
        Players, participationIndex, PlayerConnectivity, settings);
    
    % Check which constraints are violated
    sizeBool    = isempty(find(g(1:2*teamNumber) > 0, 1));
    topBool     = isempty(find(g((1:teamNumber) + 2*teamNumber) > settings.rank.UT, 1));
    downBool	= isempty(find(g((1:teamNumber) + 3*teamNumber) > settings.rank.LT, 1));
    maxBool     = isempty(find(g(end-teamNumber:end) > 0, 1)); % Smaller teams for higher rank
    
    if ~sizeBool || ~maxBool
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

%% Post-processing results
% Eliminate empty teams
finalPopulation = eliminateEmptyTeams(finalPopulation, teamNumber);

%% Display results
% Display results in command window
fprintf("No constraints:\n")
displayResults(initialPopulation, participationIndex, playerNames, ...
    optimRank, Players, PlayerConnectivity, settings)

fprintf("\n")
fprintf("Team up constraint:\n")
displayResults(finalPopulation, participationIndex, playerNames, ...
    optimRank, Players, PlayerConnectivity, settings)

fprintf("\n")

%% Pick team selection
% Choose a team
fprintf("Which team should be picked? (1) no constraints, (2) team up constraint: ")
teamChoice = input("");
fprintf("\n")
if ~any(teamChoice == [1, 2])
    teamChoice = 2; % Assign team choice if no valid answer was given
end

% Assign chosen team
if teamChoice == 1
    finalTeams(1:optimizeAmount) = initialPopulation;
else
    finalTeams(1:optimizeAmount) = finalPopulation;
end

% Put pre-assigned players back in the data
participationIndex  = [participationIndex, preAssignedTeams];
optimRank           = [optimRank, preAssignedScores];
for i = 1:exPlayers
    playerNames(optimizeAmount + i)         = preAssignedNames(i);
end

% Sort data
[participationIndex, order] = sort(participationIndex, "ascend");
finalTeams                  = finalTeams(order);
optimRank                   = optimRank(order);
playerNames                 = playerNames(order);

% Eliminate empty teams
finalTeams = eliminateEmptyTeams(finalTeams, max(finalTeams));

% Display final result
fprintf("Final teams:\n")
displayResults(finalTeams, participationIndex, playerNames, ...
    optimRank, Players, PlayerConnectivity, settings)

fprintf("\n")

if plotResults == true
    % Display team connectivity diagram
    teamConnectivityDiagram(finalTeams, playerNames)
end


%% Create file content
printName	= strings(participantAmount, 1);   % Preallocation
pCount      = 0;                            % Initialization
for i = 1:totalTeams
    % Find indices of the players in the current team
    currentPlayerIndex  = find(finalTeams == i);

    for ii = 1:length(currentPlayerIndex)
        % Update counters
        pCount	= pCount + 1;
        
        % Print to mcfunction
        printName(pCount)   = "team join Team" + num2str(i-1) + " " + ...
            playerNames(currentPlayerIndex(ii));   % Add player name to command list
    end
end

%% Write to file
filename = fullfile("Documents", "random_teams.mcfunction");    % Define document name
fileID = fopen(filename, "w");                                  % Open document
fprintf(fileID, "%s \n", printName);                            % Rewrite document
fclose(fileID);                                                 % Close document