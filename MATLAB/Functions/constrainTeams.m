function [g,h] = constrainTeams(x,scores,teamNumber,teamSize,...
    Players,ParticipantIndex,PlayerConnectivity,settings)

%% Players per team constraint
teamGroups = zeros(teamNumber,1);   % Preallocation
for i = 1:teamNumber
    teamGroups(i) = numel(x(x==i)); % Find number of players in each team
end
cp = teamGroups - teamSize; % Check if number of players per team is equal to the desired
extraUP = 1;

grep = [
    cp - extraUP  % Allow for one extra player
    -cp     % The team size may not be smaller than the specified team size
    ];

%% Team score deviation constraint
teamScore = getTeamScore(x,scores,teamNumber,teamSize,...
    Players,ParticipantIndex,settings,2);   % Get team scores

%%% Score bounds
gUp = teamScore - (mean(scores)+settings.rank.UB);       % Upper bound
gDown = -teamScore + (median(scores)-settings.rank.LB);  % Lower bound
gdev = [gUp ; gDown];

%% Played together constraint
playerNumber = length(ParticipantIndex);    % Number of participants
if ParticipantIndex(end) > length(Players)  % Check if new players are participating
    NoP = ParticipantIndex(end);
else
    NoP = length(Players);
end

%%% Define adjacency matrix
TeamCheck = zeros(NoP);
TeamCheck(sub2ind(size(TeamCheck), PlayerConnectivity(:,1), PlayerConnectivity(:,2))) = PlayerConnectivity(:,3);
TeamCheck(sub2ind(size(TeamCheck), PlayerConnectivity(:,2), PlayerConnectivity(:,1))) = PlayerConnectivity(:,3);
TeamCheck = TeamCheck(ParticipantIndex,:);
TeamCheck = TeamCheck(:,ParticipantIndex);

gTeam = zeros(playerNumber);    % Preallocation
for i = 1:playerNumber
    for ii = 1:playerNumber
        if  x(i) == x(ii)   % If players are together in a team, check how often they played together
            gTeam(i,ii) = TeamCheck(i,ii) - settings.connections;
        end
    end
end

%% Define constraints
g = [   % Inequality constraints
    grep
    gdev
    gTeam(:)
    ];

h = []; % Equality constraints
end