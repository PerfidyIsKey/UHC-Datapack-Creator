function [TeamData,Save] = TeamSelect(Match,Selection)

% Rewrite inputs
Players = Match.Players;
Ranks = Match.Ranks;
TeamCheckNorm = Match.TeamCheckNorm;
TeamCheck = Match.TeamCheck;
PlayerNum = Match.PlayerNum;
Experience = Match.Experience;
ScoreRange = Selection.ScoreRange;
MaxLoops = Selection.MaxLoops;
LoopBreak = Selection.LoopBreak;
TeamSize = Selection.TeamSize;
TotPairing = Selection.TotPairing;
ScoreNoise = Selection.ScoreNoise;

% Define quantities from inputs
MeanScore = nansum(Ranks)/PlayerNum;        % Mean score of participants
MinAvg = MeanScore - ScoreRange;            % Minimum of score interval
MaxAvg = MeanScore + ScoreRange;            % Maximum of score interval
TeamAmount = floor(PlayerNum/TeamSize);     % Amount of teams

PlayerRemain = PlayerNum;       % Preallocation - define remaining players to be equal to the total number of players
Score = zeros(TeamSize,1);      % Preallocation
Team = cell(TeamSize,1);        % Preallocation
Save = "";                      % Preallocation
ii = 1;                         % Preallocation
jj = 1;                         % Preallocation
TeamData = struct;              % Preallocation
if TeamSize == 2    % Check whether the team should consist of 2 players
    while ii <= TeamAmount
        Perm = randperm(PlayerRemain,TeamSize);     % Select players by random permutation
        WeightSize = TeamSize;                      % Reset the amount of players weighted
        for i = 1:TeamSize
            PlayerI = Perm(i);                      % Select the ith permutated player
            %             if Experience(PlayerI) == 0
            %                 WeightSize = WeightSize - 1;        % If the player is a newcomer, decrease the amount of players weighted in the team
            %             end
            if Experience(PlayerI) ~= 0
                Score(i) = max(Ranks(PlayerI) + ScoreNoise/(Experience(PlayerI))^2*randn,0);              % Pull the ith permutated player's score
            else
                Score(i) = max(Ranks(PlayerI) + ScoreNoise*randn,0);
            end
            Team{i} = Players{PlayerI};             % Pull the ith permutated player's username
        end
        
        TeamWeight = nansum(Score)/WeightSize;      % Calculate the weighted team score
        
        % Check whether the weighted team score is within the score interval,
        % whether there are no more than 1 newcomer on the team & whether the
        % players within the team have already played a set amount of time with
        % each other.
        if TeamWeight >= MinAvg && TeamWeight <= MaxAvg && WeightSize >= 1 ...
                && TeamCheck(Perm(1),Perm(2)) < TotPairing(1)
            
            for k = 1:TeamSize  % Save the command for putting these players into the correct team in Minecraft
                Save = [Save ; 'team join Team' num2str(ii-1) ' ' Team{k}];
            end
            
            % Convert for output
            TeamData(ii).Teams = Team;
            TeamData(ii).TeamWeight = TeamWeight;
            
            % Clear selected players from options
            Ranks(Perm) = [];
            Players(Perm) = [];
            PlayerRemain = size(Players,1); % Redefine amount of players left
            ii = ii + 1;    % Add a team to completed list
            jj = 1;         % Restart while loop counting
            
            % Check whether the weighted team score is within the score interval,
            % whether there are no more than 1 newcomer on the team & whether the
            % amount of loops has passed half of the maximum amount.
        elseif TeamWeight >= MinAvg && TeamWeight <= MaxAvg && WeightSize >= 1 ...
                && jj > MaxLoops/2
            
            for k = 1:TeamSize  % Save the command for putting these players into the correct team in Minecraft
                Save = [Save ; 'team join Team' num2str(ii-1) ' ' Team{k}];
            end
            
            % Convert for output
            TeamData(ii).Teams = Team;
            TeamData(ii).TeamWeight = TeamWeight;
            
            % Clear selected players from options
            Ranks(Perm) = [];
            Players(Perm) = [];
            PlayerRemain = size(Players,1); % Redefine amount of players left
            ii = ii + 1;    % Add a team to completed list
            jj = 1;         % Restart while loop counting
        else
            jj = jj + 1;    % Increase loop count if team isn't within the guidelines
        end
        
        if jj >= MaxLoops   % If the loop count exceeds the maximum amount, the interval is increased with 5 points on each side
            MinAvg = MinAvg - 5;
            MaxAvg = MaxAvg + 5;
            jj = 1;
        elseif MinAvg <= LoopBreak      % If the minimum score interval value is below a threshold, the function is terminated
            TeamData(ii).Teams = Team;
            if ~isnan(TeamWeight)
                TeamData(ii).TeamWeight = TeamWeight;
            else
                TeamData(ii).TeamWeight = 0;
            end
            Ranks(Perm) = [];
            Players(Perm) = [];
            PlayerRemain = size(Players,1);
            jj = 1;
            ii = ii + 1;
        end
    end
    
elseif TeamSize == 3        % Check whether the team should consist of 3 players
    while ii <= TeamAmount
        Perm = randperm(PlayerRemain,TeamSize);     % Select players by random permutation
        WeightSize = TeamSize;                      % Reset the amount of players weighted
        for i = 1:TeamSize
            PlayerI = Perm(i);                      % Select the ith permutated player
            %             if Experience(PlayerI) == 0
            %                 WeightSize = WeightSize - 1;        % If the player is a newcomer, decrease the amount of players weighted in the team
            %             end
            if Experience(PlayerI) ~= 0
                Score(i) = max(Ranks(PlayerI) + ScoreNoise/(Experience(PlayerI))^2*randn,0);              % Pull the ith permutated player's score
            else
                Score(i) = max(Ranks(PlayerI) + ScoreNoise*randn,0);
            end
            Team{i} = Players{PlayerI};             % Pull the ith permutated player's username
        end
        
        TeamWeight = nansum(Score)/WeightSize;      % Calculate the weighted team score
        
        % Check whether the weighted team score is within the score interval,
        % whether there are no more than 1 newcomer on the team & whether the
        % players within the team have already played a set amount of time with
        % each other.
        if TeamWeight >= MinAvg && TeamWeight <= MaxAvg && WeightSize >= 2 ...
                && TeamCheckNorm(Perm(1),Perm(2)) + TeamCheckNorm(Perm(1),Perm(3)) + ...
                TeamCheckNorm(Perm(2),Perm(3)) < TeamSize && TeamCheckNorm(Perm(1),Perm(2))...
                + TeamCheckNorm(Perm(1),Perm(3)) < TeamSize-1 && TeamCheckNorm(Perm(1),Perm(2))...
                + TeamCheckNorm(Perm(2),Perm(3)) < TeamSize-1 && TeamCheckNorm(Perm(1),Perm(3))...
                + TeamCheckNorm(Perm(2),Perm(3)) < TeamSize-1 && TeamCheck(Perm(1),Perm(3)) + ...
                TeamCheck(Perm(1),Perm(2)) + TeamCheck(Perm(2),Perm(3)) < TotPairing(2)
            
            for k = 1:TeamSize  % Save the command for putting these players into the correct team in Minecraft
                Save = [Save ; 'team join Team' num2str(ii-1) ' ' Team{k}];
            end
            
            % Convert for output
            TeamData(ii).Teams = Team;
            TeamData(ii).TeamWeight = TeamWeight;
            
            % Clear selected players from options
            Ranks(Perm) = [];
            Players(Perm) = [];
            PlayerRemain = size(Players,1); % Redefine amount of players left
            ii = ii + 1;    % Add a team to completed list
            jj = 1;         % Restart while loop counting
            
            % Check whether the weighted team score is within the score interval,
            % whether there are no more than 1 newcomer on the team & whether the
            % amount of loops has passed half of the maximum amount.
        elseif TeamWeight >= MinAvg && TeamWeight <= MaxAvg && WeightSize >= 2 ...
                && jj > MaxLoops/2
            
            for k = 1:TeamSize  % Save the command for putting these players into the correct team in Minecraft
                Save = [Save ; 'team join Team' num2str(ii-1) ' ' Team{k}];
            end
            
            % Convert for output
            TeamData(ii).Teams = Team;
            TeamData(ii).TeamWeight = TeamWeight;
            
            % Clear selected players from options
            Ranks(Perm) = [];
            Players(Perm) = [];
            PlayerRemain = size(Players,1); % Redefine amount of players left
            ii = ii + 1;    % Add a team to completed list
            jj = 1;         % Restart while loop counting
        else
            jj = jj + 1;    % Increase loop count if team isn't within the guidelines
        end
        
        if jj >= MaxLoops   % If the loop count exceeds the maximum amount, the interval is increased with 5 points on each side
            MinAvg = MinAvg - 5;
            MaxAvg = MaxAvg + 5;
            jj = 1;
        elseif MinAvg <= LoopBreak      % If the minimum score interval value is below a threshold, the function is terminated
            TeamData(ii).Teams = Team;
            if ~isnan(TeamWeight)
                TeamData(ii).TeamWeight = TeamWeight;
            else
                TeamData(ii).TeamWeight = 0;
            end
            Ranks(Perm) = [];
            Players(Perm) = [];
            PlayerRemain = size(Players,1);
            jj = 1;
            ii = ii + 1;
        end
    end
end
end