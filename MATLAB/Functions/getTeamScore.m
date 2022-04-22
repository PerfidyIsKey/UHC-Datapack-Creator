function teamScore = getTeamScore(x,scores,teamNumber,teamSize,...
    Players,ParticipantIndex,settings,mode)
%% Initialization
unitsNumber = length(x);            % Number of players
teamScore = zeros(teamNumber,1);    % Preallocation
switch mode
    case 1  % Objective function
        power = 4;
    case 2  % Constraints
        power = 1;
end

%% Determine team scores
for i = 1:teamNumber
    count = 0;  % Initialize counter
    bestPlayer = 0;
    for ii = 1:unitsNumber
        if x(ii) == i % If player is in this team, add their score
            count = count + 1;  % Update counter
%             scoreNoise = settings.noise/(Players(ParticipantIndex(ii)).Experience)^2*randn; % Add score noise
%             playerScore = max(scores(ii)+scoreNoise,0);
playerScore = scores(ii);
            teamScore(i) = teamScore(i) + playerScore^power;   % Update team score
            bestPlayer = max(bestPlayer,playerScore);
            if count > teamSize % If extra players are added, add a score of a pseudo-team mate
                meanScore = max(bestPlayer,100)^power;
                teamScore(i) = teamScore(i) + meanScore;
            end
        end
    end
    
    if count < teamSize
        teamScore(i) = teamScore(i) - (teamSize-count)*mean(scores)^power;
    end
    
    if count > 0    % If the team has players assigned, scale the score
        teamScore(i) = teamScore(i)/count^power;
    else
        teamScore(i) = 1e9;
    end
end

%% Scale the score to first order
teamScore = (teamScore).^(1/power);
end