function [state, options, flag] = gaoutfun(options, state, flag, playerName)
% GAOUTFUN Custom output function for Genetic Algorithm
% This function displays information during the execution of the genetic algorithm.
%
% Output:
%   state: Current state of the Genetic Algorithm
%   options: Options set for the Genetic Algorithm
%   flag: Termination flag for the Genetic Algorithm

switch flag
    case "done"
        % Display the end results in terms of a connectivity diagram
        fprintf("Algorithm successfully executed!\n")

        % Define color array
        colors = [
            255 255   0;   % Yellow
            0   0 255;   % Blue
            255   0   0;   % Red
            128   0 128;   % Purple
            0 128   0;   % Green
            255 192 203;   % Pink
            0   0   0;   % Black
            255 165   0;   % Orange
            128 128 128;   % Gray
            0 255 255;   % Aqua
            139   0   0;   % Dark Red
            0   0 139;   % Dark Blue
            0 139 139;   % Dark Aqua
            0 100   0;   % Dark Green
            169 169 169;   % Dark Gray
            255 255 255    % White
            ]/255;


        %%% Visualize
        currentFigure = findobj("Tag", "teamDisplay");
        if isempty(currentFigure)
            %% Create random positions
            NoP         = length(playerName);                           % Number of players
            maxCanvas   = 2;                                            % Maximum plot size
            X           = cos(maxCanvas*pi*(1:NoP)'/NoP) + maxCanvas/2; % Vertex x-coordinates
            Y           = sin(maxCanvas*pi*(1:NoP)'/NoP) + maxCanvas/2; % Vertex y-coordinates
            shuffle     = randperm(NoP);                                % Randomly shuffle the coordinates
            coords      = [X(shuffle),  Y(shuffle)];                    % Assign coordinates to each player
            headOffset  = 0.12;                                         % Offset to plot the images correctly

            %% Initialize figure
            figure("Name", "Final team composition", "Tag", "teamDisplay")
            hold("on")
            plot(coords(:,1), coords(:,2), "Visible", false, "Tag", "connections")
            axis("equal")
            ax          = gca;
            xlim([0, maxCanvas] + headOffset*[-1, +1])
            ylim([0, maxCanvas] + headOffset*[-1, +1])
            set(gca, "Visible", "off")

            % Player names
            for i = 1:NoP
                if coords(i, 2) <= 1
                    text(ax, coords(i, 1), coords(i, 2) - headOffset, playerName(i), "Interpreter", "none",...
                        "HorizontalAlignment", "center")
                else
                    text(ax, coords(i, 1), coords(i, 2) + headOffset, playerName(i), "Interpreter", "none",...
                        "HorizontalAlignment", "center")
                end
            end

            % Player icons
            for i = 1:NoP
                % Load in image
                currentImage = retrievePlayerHead(playerName{i});
    
                % Specify coordinates
                xCoor = coords(i, 1) + headOffset*[1/2, -1/2];
                yCoor = coords(i, 2) + headOffset*[1/2, -1/2];

                % Visualize image
                image(xCoor, yCoor, currentImage)
            end

        else
            %% Initialize correct figure
            figure(currentFigure)
            ax = gca;
            ax = removeLines(ax);        % Remove old team lines
        end

        %% Create connectivity diagram
        % Identify best population
        [~,bestGene]    = min(state.Score);
        bestPopulation  = state.Population(bestGene, :);

        % Define global player coordinates
        xCoor = get(findobj("Tag", "connections"), "XData");
        yCoor = get(findobj("Tag", "connections"), "YData");

        % Loop through every team
        processFlag = true;
        count = 1;
        while processFlag
            % Find players in current team
            currentPlayers = find(bestPopulation == count);

            % Retrieve current player coordinates
            xCurrent = xCoor(currentPlayers);
            yCurrent = yCoor(currentPlayers);

            % Create adjacency matrix
            A = ones(length(currentPlayers)) - eye(length(currentPlayers));

            % Create connectivity data
            [xG, yG] = gplot(A, [xCurrent', yCurrent']);

            % Plot data
            plot(ax, xG, yG, "Color", colors(count, :))

            % Update counter
            count = count + 1;

            % Break loop
            if count > max(bestPopulation)
                break
            end
        end

end

% Update the figure
drawnow
end


function ax = removeLines(ax)
% Get all children (objects) of the specified axis 'a'
children = get(ax, 'Children');

% Initialize an empty array to store the Line objects to be removed
linesToRemove = [];

% Iterate through the children and identify Line objects
for i = 1:length(children)
    if isgraphics(children(i), "Line") && get(children(i), "Tag") ~= "connections"
        % If the child is a Line object, add it to the list of lines to remove
        linesToRemove = [linesToRemove, children(i)];
    end
end

% Remove the identified Line objects from the axis 'a'
delete(linesToRemove);
end

function img = retrievePlayerHead(playerName)
% Define the URL of the webpage containing the image
urlBase = "https://mc-heads.net/avatar/";
url     = urlBase + playerName;

% Read the webpage content
img = imread(url);
end