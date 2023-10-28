clear
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% The Diorite Experts UltraHardCore - Rank Addition
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This code calculates the new data after each season. It loads the old
% data, then makes calculations with the provided statistics and finally
% saves the new data file.

%% Load data
addpath('Data','Functions')

% The following variables are preallocated to get rid of the warnings
% below. The preallocation does not do anything as the data will be loaded
% out of the mat file anyways.
Players             = struct;       % Preallocation
Seasons             = struct;       % Preallocation
PlayerConnectivity	= zeros(1,3);   % Preallocation
load('DataS50.mat')                    % Load data

%% Define season data
%%% Season data
NoS                     = size(Seasons,2) + 1;  % New season index
Seasons(NoS).Season     = 'S51';    % Season name
Seasons(NoS).Players    = 11;                    % Total number of players
Seasons(NoS).Date       = datetime(2023,10,28); % Date of the season
Seasons(NoS).TeamAmount = 3;	% Needs to be equal to the amount of members in the winning team

%%% New Players
PlayersNew = ["mrminebase", "JOPONOS"];     % Define the username of new players

NoN     = length(PlayersNew);	% Number of new players
NoP     = length(Players);      % Old total number of players
NoPN	= NoP + NoN;            % New total number of players

NewIndex = zeros(NoN,1);    % Preallocation
for i = 1:NoN
    NewIndex(i) = NoP + i;  % Store the indices of the new players
end

%%% Player data
ParticipantIndex	= [01,02,17,18,25,31,48,52,54,NewIndex'];    % Define index of the participants
Kills               = [00;00;01;00;00;00;02;01;00;00;02];              % Indicate amount of kills per player
Position            = [07;01;01;10;09;08;01;05;05;11;06];              % Indicate final position
Winner              = [00;01;01;00;00;00;01;00;00;00;00];              % Indicate whether the player won the season
NoPar               = size(ParticipantIndex,2);             % Indicate number of participants

PC = [      % Indicate which players were in a team together
    1 52
    1 55
    2 17
    2 48
    17 48
    18 25
    18 31
    25 31
    52 55
    54 56
    ];

%% Update data
%%% Update the player connectivity
NoT = size(PC,1);                   % Number of connections during the new season
% NoC = size(PlayerConnectivity,1);   % Number of existing connections
NoC = 0;
for i = 1:NoT
    success = 0;    % Initialization
    Player1 = find(PlayerConnectivity(:,1) == PC(i,1)); % Find the player in the data set
    if ~isempty(Player1)        % If this player is in the data set already
        NoF = size(Player1,1);	% Define the amount of players they are connected to
        for ii = 1:NoF
            if PlayerConnectivity(Player1(ii),2) == PC(i,2)
                % If these players have played together before, increase
                % the weight between their connection
                PlayerConnectivity(Player1(ii),3) = ...
                    PlayerConnectivity(Player1(ii),3) + 1;
                success = 1;    % Indicate successful addition
            end
            if success == 1
                % If the correct connection was found, the loop is broken
                break
            end
        end
        if success ~= 1
            % If no existing connection is found, the connection is added
            % to the data set.
            if Player1 ~= 1
                NoC = NoC + 1;  % Increase the amount of connections
            else
                NoC = size(PlayerConnectivity,1);
            end
            for ii = NoC:-1:Player1(NoF)+1
                % Move all entries one row down to make space for the new
                % connection
                PlayerConnectivity(ii,1) = PlayerConnectivity(ii-1,1);
                PlayerConnectivity(ii,2) = PlayerConnectivity(ii-1,2);
                PlayerConnectivity(ii,3) = PlayerConnectivity(ii-1,3);
            end
            % Add the new connection to the data set
            PlayerConnectivity(Player1(NoF)+1,1) = PC(i,1);
            PlayerConnectivity(Player1(NoF)+1,2) = PC(i,2);
            PlayerConnectivity(Player1(NoF)+1,3) = 1;
            Player1(NoF+1)	= Player1(NoF) + 1; % Assign the indices of the new entries
            % Sort the data in ascending order
            [~,SortIndex]   = sort(PlayerConnectivity(Player1(1):Player1(NoF)+1,2),'ascend');
            PlayerConnectivity(Player1(1):Player1(NoF)+1,2) =...
                PlayerConnectivity(Player1(SortIndex),2);
            PlayerConnectivity(Player1(1):Player1(NoF)+1,3) =...
                PlayerConnectivity(Player1(SortIndex),3);
        end
    else    % If the player is not yet in the data set add them
        NoC = size(PlayerConnectivity,1);	% Number of existing connections
        PlayerResidue = find(PC(i,1) < PlayerConnectivity(:,1));    % Check which players would be underneath the new player in the data set
        if ~isempty(PlayerResidue)
            % If the new connection should be somewhere in the data set,
            % add it in the correct place
            NoF = find(PC(i,1) > PlayerConnectivity(:,1),1,'last'); % Find the entry before the the new connection
            NoC = NoC + 1;  % Increment the amount of connections
            for ii = NoC:-1:NoF+1
                % Move all entries one row down to make space for the new
                % connection
                PlayerConnectivity(ii,1) = PlayerConnectivity(ii-1,1);
                PlayerConnectivity(ii,2) = PlayerConnectivity(ii-1,2);
                PlayerConnectivity(ii,3) = PlayerConnectivity(ii-1,3);
            end
            % Add the new connection to the data set
            PlayerConnectivity(NoF+1,1) = PC(i,1);
            PlayerConnectivity(NoF+1,2) = PC(i,2);
            PlayerConnectivity(NoF+1,3) = 1;
        else
            % If the new connection is bigger than the current biggest
            % connection, add it to the final row
            PlayerConnectivity(NoC+1,1)  = PC(i,1);
            PlayerConnectivity(NoC+1,2)  = PC(i,2);
            PlayerConnectivity(NoC+1,3)  = 1;
        end
    end
end

%%% Update individual players
% Preallocate for all old players
for i = 1:NoP
    Players(i).Kills(NoS,:)           = 0;    % Kills
    Players(i).Position(NoS,:)        = NaN;  % Finishing position
    Players(i).Winner(NoS,:)          = 0;    % Indicate winner
    Players(i).Participation(NoS,:)	= 0;    % Participation
end

% Preallocate for new players
for i = NoP+1:NoPN
    Players(i).Kills            = zeros(NoS,1);         % Kills
    Players(i).PlayerName       = PlayersNew{i-NoP};    % Playername
    Players(i).Position         = NaN(NoS,1);           % Finishing position
    Players(i).Winner           = zeros(NoS,1);         % Indicate winner
    Players(i).Participation	= zeros(NoS,1);         % Participation
    Players(i).RankHistory      = NaN(NoS,1);           % Rank history
    Players(i).RankPosition     = NaN(NoS,1);           % Rank position
end

% Assign for participants
for i = 1:NoPar
    Players(ParticipantIndex(i)).Kills(NoS,:)         = Kills(i);     % Kills
    Players(ParticipantIndex(i)).Position(NoS,:)      = Position(i);  % Finishing position
    Players(ParticipantIndex(i)).Winner(NoS,:)        = Winner(i);    % Indicate winner
    Players(ParticipantIndex(i)).Participation(NoS,:)	= 1;            % Participation
    Players(ParticipantIndex(i)).Active             = 1;            % Label player as active
end

%%% Update Rank
Players = RankDetermine(Players,Seasons);

% Add current rank to rank history
for i = 1:NoPN
    Players(i).RankHistory(NoS,:) = Players(i).Rank;
end

%% Save data
% If desired, save the data into a .mat file
fprintf('Would you like to save the data?\n')
fprintf('1\tYES\n')
fprintf('2\tYES (dummy save)\n')
fprintf('3\tNO\n')
saveData = input('Make your choice: ');
if saveData == 1
    save(['Data\' 'Data.mat'],'Players','Seasons','PlayerConnectivity')
elseif saveData == 2
    save(['Data\' 'Data' Seasons(NoS).Season '.mat'],'Players','Seasons','PlayerConnectivity')
end