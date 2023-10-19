clear
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% THE DIORITE EXPERTS ULTRAHARDCORE EXPORT SEASONS DATA %%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Writes season data from a .mat file to a .txt file. The following format
% is used: identifier, number of players, year, month, day.

%% Import data
loadName = "DataS50.mat";   % Indicate .mat data file
load(fullfile("Data", loadName), "Seasons")

%% Finding constant parameters
seasonNumber        = length(Seasons);              % Total number of seasons
headerSize          = 1;                            % Size of the header
totalLines          = seasonNumber + headerSize;    % Total number of lines to write

%% Load file to write to
saveName = "seasonData.txt"; % Indicate .txt file
fileID = fopen(fullfile("Documents", saveName), "w");

%% Define text to write
text = strings(totalLines, 1);  % Preallocation

% Header
text(1) =  "// Format: Identifier, number of players, year, month, day";

% Player indexing
for i = 1:seasonNumber
    index = i + headerSize;
    text(index) = lower(string(Seasons(i).Season)) + "," + num2str(Seasons(i).Players, "%1.0f") +...
        "," + string(Seasons(i).Date, "yyyy,MM,dd"); 
end

%% Save text to file
fprintf(fileID, "%s \n", text);   % Rewrite document
fclose(fileID);                     % Close document