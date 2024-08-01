function teamScore = getTeamScore(x, scores, teamNumber, teamSize)
%% Initialization
unitsNumber = length(x);            % Number of players
teamScore   = zeros(teamNumber, 1); % Preallocation

%% Determine team scores
for i = 1:teamNumber
    count       = 0;  % Initialize counter
    bestPlayer  = 0;
    for ii = 1:unitsNumber
        if x(ii) == i % If player is in this team, add their score
            count           = count + 1;                        % Update counter
            playerScore     = scores(ii);                       % Retrieve rank
            teamScore(i)    = teamScore(i) + playerScore; % Update team score
            bestPlayer      = max(bestPlayer, playerScore);     % Determine best rank in team
            if count > teamSize % If extra players are added, add a score of a pseudo-team mate
                meanScore       = max(bestPlayer, 100);
                teamScore(i)    = teamScore(i) + meanScore;
            end
        end
    end

    if count > 0    % If the team has players assigned, scale the score
        if count >= teamSize
            teamScore(i) = teamScore(i)/count;
        else
            teamScore(i) = teamScore(i)/teamSize;
        end
    else
        teamScore(i) = mean(scores);
    end
end
end