function displayResults(playerInput, participantIndex, playerName, scores, ...
    Players, playerConnectivity, settings)

% Team colors
colVec = ["Yellow", "Blue", "Red", "Purple", "Green", "Pink", "Black", "Orange", "Gray",...
    "Aqua", "D.Red", "D.Blue", "D.Aqua", "D.Green", "D.Gray", "White"];

% Get sizes
teamSize        = settings.players; % Number of players per team
teamNumber      = max(playerInput); % Number of teams

% Get team scores
teamScore = getTeamScore(playerInput, scores, teamNumber, teamSize, 2);

% Loop through all teams
for i = 1:teamNumber
    % Find indices of the players in the current team
    currentPlayerIndex  = find(playerInput == i);
    thisTeamAmount      = length(currentPlayerIndex);   % Number of players in current team
   
    % Create connectivity adjacency matrix
    A = createConnectivityAdjancencyMatrix(max([participantIndex, length(Players)]),...
        playerConnectivity, participantIndex(currentPlayerIndex));

    % String preallocation
    names = strings(1, thisTeamAmount);

    % Loop through all players in the team
    for ii = 1:length(currentPlayerIndex)
        % Include maximum connections in name
        mostConnections = max(A(ii,:));
        names(ii) = playerName(currentPlayerIndex(ii)) + " {" + ...
        num2str(mostConnections) + "}";

        % Update display operators
        if ii == 1
            oper = '%s';
        else
            oper((end + 1):(end + 4)) = ', %s';
        end
    end

    % Convert scores to strings
    score       = num2str(teamScore(i), "%3.0f");
    devScore	= num2str(teamScore(i) - mean(scores), "%+2.0f");
    printChar = "Team %7s: " + oper + " Team score: %s (%s)\n";

    % Print team list
    fprintf(printChar, [colVec(i), names, score, devScore])
end

end