clear
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%% THE DIORITE EXPERTS ULTRAHARDCORE EXPORT PLAYERCONNECTIVITY DATA %%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Writes player connectivity data from a .mat file to a .txt file. The
% first column indicates the first player. The second column indicates
% which player they are connected to. The third column indicates how many
% times they played together. Player indices are given in the comments.

%% Import data
loadName = "DataS52.mat";   % Indicate .mat data file
load(fullfile("Data", loadName), "Players", "PlayerConnectivity")

%% Finding constant parameters
playerNumber        = length(Players);                      % Total number of players
connectionNumber    = size(PlayerConnectivity, 1);          % Total number of connections
headerSize          = 4;                                    % Size of the header
totalLines          = playerNumber + connectionNumber +...  % Total number of lines to write
    headerSize;

%% Load file to write to
saveName = "playerConnections.txt"; % Indicate .txt file
fileID = fopen(fullfile("Documents", saveName), "w");

%% Define text to write
text = strings(totalLines, 1);  % Preallocation

% Header
text(1:4) = [
    "// The first column indicates the first player."
    "// The second column indicates which player they are connected to."
    "// The third column indicates how many times they played together."
    "// Player indices are given below."
    ];

% Player indexing
for i = 1:playerNumber
    index = i + headerSize;
    text(index) = "// " + num2str(i, "%1.0f") + " " + Players(i).PlayerName; 
end

% Player connectivity
for i = 1:connectionNumber
    index       = playerNumber + headerSize + i;
    text(index) = num2str(PlayerConnectivity(i,1),"%1.0f") + "," +...
        num2str(PlayerConnectivity(i,2),"%1.0f") + "," +...
        num2str(PlayerConnectivity(i,3),"%1.0f");
end

%% Save text to file
fprintf(fileID, "%s \n", text);   % Rewrite document
fclose(fileID);                     % Close document