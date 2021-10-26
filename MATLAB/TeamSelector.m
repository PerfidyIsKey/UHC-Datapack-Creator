clear
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% UltraHardCore Team Selector
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This code was made to randomly assemble teams for The Diorite Experts
% UltraHardCore. However, this code can also be used for different UHC
% competitions, providing a similar ranking system is adopted.
% See more at: https://github.com/Snodog627/TheDioriteExpertsUHC

% The code at first defines the players and their scores. The players that
% are participating in the current season are then selected and some
% simulation criteria may be specified. New players' scores are denoted by
% NaN.

%% Information
addpath('Data','Functions','Documents')
load('DataURE4.mat')

% Enter the players that are participating (corresponding numbers with
% PlayerName variable in Players struct)
% ParticipantIndex = [1,2,17,18,24,25,27,28,30,31,33,34,36,38,39,40,41,42];
ParticipantIndex = [4,5,6,15,17,18:26];

% Enter the names of new players
NewPlayers = {};
EstimatedRank = [];

NoN = size(NewPlayers,2);
NoP = size(Players,2) + NoN;
NoPar = size(ParticipantIndex,2);    % Number of players

% Unit adjacency matrix
TeamCheckNorm = zeros(NoP,NoP);
TeamCheckNorm(sub2ind(size(TeamCheckNorm), PlayerConnectivity(:,1), PlayerConnectivity(:,2))) = 1;
TeamCheckNorm(sub2ind(size(TeamCheckNorm), PlayerConnectivity(:,2), PlayerConnectivity(:,1))) = 1;

% Adjacency matrix
TeamCheck = zeros(NoP,NoP);
TeamCheck(sub2ind(size(TeamCheckNorm), PlayerConnectivity(:,1), PlayerConnectivity(:,2))) = PlayerConnectivity(:,3);
TeamCheck(sub2ind(size(TeamCheckNorm), PlayerConnectivity(:,2), PlayerConnectivity(:,1))) = PlayerConnectivity(:,3);

%% Participation criteria
% Pull the players that are participating in this season
PlayerName = cell(NoPar+NoN,1);
Ranks = zeros(NoPar+NoN,1);
Experience = zeros(NoPar+NoN,1);
for i = 1:NoPar + NoN
    if i <= NoPar
        PlayerName{i} = Players(ParticipantIndex(i)).PlayerName;
        Ranks(i) = Players(ParticipantIndex(i)).Rank;
        Experience(i) = sum(Players(ParticipantIndex(i)).Participation);
    else
        PlayerName{i} = NewPlayers{i - NoPar};
        Ranks(i) = EstimatedRank(i-NoPar);
        ParticipantIndex(i) = NoP - NoN + i - NoPar;
        Experience(i) = 0;
    end
end

TeamCheckNorm = TeamCheckNorm(ParticipantIndex,:);
TeamCheckNorm = TeamCheckNorm(:,ParticipantIndex);
TeamCheck = TeamCheck(ParticipantIndex,:);
TeamCheck = TeamCheck(:,ParticipantIndex);

NoP = size(PlayerName,1);            % Number of players
MeanScore = nansum(Ranks)/NoP;    % Mean score among the current set of players

%% Team selection
% Team criteria
ScoreRange = 10;        % Allowed deviation from the mean score
MaxLoops = 1000;        % Maximum number of loops before the interval is increased
LoopBreak = 0;          % Break the loop if the minimum value of the score interval subceeds this value
TeamSize = 2;           % Amount of players in one team
TotPairing = [0 1];     % Threshold of how many times players in a team can have teamed up
ScoreTol = 35;          % Tolerance of the final mean score
ScoreNoise = 60;
TeamAmount = floor(NoP/TeamSize); % Amount of teams

%% Struct conversion
%%% Match
Match.Players = PlayerName;
Match.Ranks = Ranks;
Match.TeamCheckNorm = TeamCheckNorm;
Match.TeamCheck = TeamCheck;
Match.PlayerNum = NoP;
Match.Experience = Experience;

%%% Selection
Selection.ScoreRange = ScoreRange;
Selection.MaxLoops = MaxLoops;
Selection.LoopBreak = LoopBreak;
Selection.TeamSize = TeamSize;
Selection.TotPairing = TotPairing;
Selection.ScoreNoise = ScoreNoise;

%% Function Calling
MaxCount = 100;     % Maximum number of loop iterations
LoopFail = true;    % Preallocation
WhileCount = 1;     % Preallocation
TeamWeight = zeros(TeamAmount,1);   % Preallocation
ScoreDeviation = zeros(TeamAmount,1);   % Preallocation
while LoopFail
    [TeamData,Save] = TeamSelect(Match,Selection);  % Select the teams
    
    for i = 1:TeamAmount
        TeamWeight(i) = TeamData(i).TeamWeight;
        ScoreDeviation(i) = TeamWeight(i) - MeanScore;
    end
    LoopFail = min(TeamWeight) < MeanScore - ScoreTol...
        || max(TeamWeight) > MeanScore + ScoreTol;  % Check whether all team scores are within the tolerance
    WhileCount = WhileCount + 1; % Increase loop iteration
    if WhileCount > MaxCount    % Display an error if the maximum number of loops is exceeded
        error('Teams could not be generated within the constraints, please increase the score tolerance.')
    end
    
    
end

%% Reveal Data
% Display the data in the Command Window
ColVec = ["Yellow";"Blue";"Red";"Purple";"Green";"Pink";"Black";"Orange";"Gray";...
    "Aqua";"D.Red";"D.Blue";"D.Aqua"];
fprintf('The mean score of all players is %3.0f.\n',MeanScore)
for ii = 1:TeamAmount
    if TeamSize == 2
        fprintf('Team %s\tconsists of ',ColVec{ii})
        fprintf('%16s and ',TeamData(ii).Teams{1})
        fprintf('%16s. ',TeamData(ii).Teams{2})
        fprintf('Their weighted team score is %3.0f (%+2.0f)\n',[TeamData(ii).TeamWeight ScoreDeviation(ii)])
    elseif TeamSize == 3
        fprintf('Team %s\tconsists of ',ColVec{ii})
        fprintf('%16s, ',TeamData(ii).Teams{1})
        fprintf('%16s and ',TeamData(ii).Teams{2})
        fprintf('%16s. ',TeamData(ii).Teams{3})
        fprintf('Their weighted team score is %3.0f (%+2.0f)\n',[TeamData(ii).TeamWeight ScoreDeviation(ii)])
    end
end

%% Write data to mcfunction file
Save(1) = [];                           % Delete preallocation value
filename = 'random_teams.mcfunction';   % Define document name
fileID = fopen(['Documents\' filename],'w');           % Open document
fprintf(fileID,'%s \n',Save);           % Rewrite document
fclose(fileID);                         % Close document