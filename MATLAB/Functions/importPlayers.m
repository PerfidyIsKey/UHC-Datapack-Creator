function [participantIndex, playerName, rank] = importPlayers(filename, dataLines)
%IMPORTFILE Import data from a text file
%  PLAYERS = IMPORTFILE(FILENAME) reads data from text file FILENAME for
%  the default selection.  Returns the data as a table.
%
%  PLAYERS = IMPORTFILE(FILE, DATALINES) reads data for the specified
%  row interval(s) of text file FILENAME. Specify DATALINES as a
%  positive scalar integer or a N-by-2 array of positive scalar integers
%  for dis-contiguous row intervals.
%
%  Example:
%  players = importfile("C:\Users\bthem\Documents\UHC-Datapack-Creator\Files\DIORITE\players.txt", [1, Inf]);
%
%  See also READTABLE.
%
% Auto-generated by MATLAB on 12-Mar-2024 12:24:17

%% Input handling

% If dataLines is not specified, define defaults
if nargin < 2
    dataLines = [1, Inf];
end

%% Set up the Import Options and import the data
opts = delimitedTextImportOptions("NumVariables", 5);

% Specify range and delimiter
opts.DataLines = dataLines;
opts.Delimiter = ",";

% Specify column names and types
opts.VariableNames = ["identifier", "playerName", "rank", "lastTraitor", "participating"];
opts.VariableTypes = ["double", "string", "double", "double", "logical"];

% Specify file level properties
opts.ExtraColumnsRule = "ignore";
opts.EmptyLineRule = "read";

% Specify variable properties
opts = setvaropts(opts, "playerName", "WhitespaceRule", "preserve");
opts = setvaropts(opts, ["playerName", "participating"], "EmptyFieldRule", "auto");

% Import the data
players = readtable(filename, opts);

% Retrieve participating players
participatingPlayers = players{:, 5} == true;
participantIndex = players{participatingPlayers, 1}';

% Retrieve player names
playerName = players{participatingPlayers, 2};

% Retrieve ranks
rank = players{participatingPlayers, 3}';

end