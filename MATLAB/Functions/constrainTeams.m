function [g, h] = constrainTeams(x, scores, teamNumber, teamSize,...
    Players, participantIndex, playerConnectivity, settings)

%% Players per team constraint
teamGroups = zeros(teamNumber, 1);   % Preallocation
for i = 1:teamNumber
    teamGroups(i) = numel(x(x == i)); % Find number of players in each team
end
cp = teamGroups - teamSize; % Check if number of players per team is equal to the desired
extraUP = 1;
if teamSize > 2
    extraDOWN = 1;
else
    extraDOWN = 0;
end

grep = [
    cp - extraUP    % Allow for one extra player
    -cp - extraDOWN % The team size may not be smaller than the specified team size
    ];

%% Team score deviation constraint
teamScore = getTeamScore(x, scores, teamNumber, teamSize);   % Get team scores

%%% Score bounds
gUp = teamScore - (mean(scores) + settings.rank.UB);        % Upper bound
gDown = -teamScore + (median(scores) - settings.rank.LB);   % Lower bound
gdev = [gUp ; gDown];

%% Played together constraint
participantNumber = length(participantIndex);   % Number of participants
if participantIndex(end) > length(Players)      % Check if new players are participating
    playerNumber = participantIndex(end);
else
    playerNumber = length(Players);
end

%%% Define adjacency matrix
A = createConnectivityAdjancencyMatrix(playerNumber, playerConnectivity, ...
    participantIndex);

gTeam = zeros(participantNumber);    % Preallocation
for i = 1:participantNumber
    for ii = 1:participantNumber
        if  x(i) == x(ii)   % If players are together in a team, check how often they played together
            gTeam(i, ii) = A(i, ii) - settings.connections;
        end
    end
end

%%  Only allow players with a rank of over 100 to have a smaller team
% Define maximum rank for getting a team penalty
maxRank = 100;

gGood = zeros(teamNumber, 1);   % Preallocation
for i = 1:teamNumber
    % Loop through all teams
    thesePlayers = x == i;              % Players in current team

    if length(thesePlayers) < teamSize
        % Only look at smaller teams
        theseRanks = scores(thesePlayers);  % Ranks of players in current team
        if any(theseRanks >= maxRank)
            gGood(i) = 0;                   % Allow small teams for skilled players
        else
            gGood(i) = 1;                   % Refuse small teams for lower-rated players
        end
    else
        gGood(i) = 0;   % Team is not smaller
    end
end

%% Define constraints
g = [   % Inequality constraints
    grep
    gdev
    gTeam(:)
    gGood
    ];

h = []; % Equality constraints
end