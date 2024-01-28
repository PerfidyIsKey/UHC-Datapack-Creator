clear
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% THE DIORITE EXPERTS ULTRAHARDCORE EXPORT PLAYERS DATA %%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Writes players data from a .mat file to a .txt file. 

%% Import data
loadName = "DataS52.mat";   % Indicate .mat data file
load(fullfile("Data", loadName), "Players", "Seasons")

%% Finding constant parameters
playerNumber        = length(Players);              % Total number of players
seasonNumber        = length(Seasons);              % Total number of seasons
headerSize          = 1;                            % Size of the header
totalLines          = playerNumber + headerSize;    % Total number of lines to write

%% Load file to write to
saveName = "playerData.txt"; % Indicate .txt file
fileID = fopen(fullfile("Documents", saveName), "w");

%% Define text to write
text        = strings(totalLines, 1);   % Preallocation
startString = "[";                      % Initialization start array string
endString   = "]";                      % Initialization end array string

% Header
text(1) =  "// Format: Identifier, participation array, position array, kills array, winner array";

% Player indexing
for i = 1:playerNumber
    % Correct assignment index
    index = i + headerSize;

    % Player identifier string
    identifierString = num2str(i, "%1.0f");

    % Create individual array strings
    [participationString, positionString, killString, winnerString] = deal(startString);
    for j = 1:seasonNumber
        % Participation string
        if Players(i).Participation(j) == 1
            thisParticipationString = "true";
        else
            thisParticipationString = "false";
        end

        % Position string
        thisPositionString = num2str(Players(i).Position(j), "%1.0f");

        % Kills string
        thisKillString = num2str(Players(i).Kills(j), "%1.0f");

        % Winner string
        if Players(i).Winner(j) == 1
            thisWinnerString = "true";
        else
            thisWinnerString = "false";
        end

        % Update strings
        participationString = participationString + thisParticipationString + ",";
        killString          = killString + thisKillString + ",";
        positionString      = positionString + thisPositionString + ",";
        winnerString        = winnerString + thisWinnerString + ",";


        % Remove trailing comma and close array
        if j == seasonNumber
            participationString = replaceFinalCharacter(participationString, endString);
            killString          = replaceFinalCharacter(killString, endString);
            positionString      = replaceFinalCharacter(positionString, endString);
            winnerString        = replaceFinalCharacter(winnerString, endString);
        end
    end

    % Combine strings
    text(index) = identifierString + "," + participationString + "," + killString +...
        "," + positionString + "," + winnerString;
end

%% Save text to file
fprintf(fileID, "%s \n", text);   % Rewrite document
fclose(fileID);                     % Close document

%% Function
function newString = replaceFinalCharacter(oldString, replacementCharacter)
% Turn string into char array
newString = char(oldString);

% Change final character of old string
newString(end) = replacementCharacter;

% Convert back to string array format
newString = string(newString);
end