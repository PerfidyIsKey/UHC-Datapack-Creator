clear
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% The Diorite Experts UltraHardCore - Visualize player connections
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This script visualizes which players have played together before with the
% use of an adjacency matrix.

%% Load data
addpath('Data','Emotes')	% Add playerhead directory
load('DataS45B.mat')            % Load data
ActiveOnly = 1;           % Only display active players

%% Create adjacency matrix
NoP = size(Players,2);              % Number of players
PlayersActive = [];
for i = 1:NoP
    if Players(i).Active == 1
        PlayersActive = [PlayersActive ; i];
    end
end
NoA = size(PlayersActive,1);        % Number of active players
NoC = size(PlayerConnectivity,1);   % Number of connections

% Adjacency matrix
TeamCheck = zeros(NoP,NoP);
TeamCheck(sub2ind(size(TeamCheck), PlayerConnectivity(:,1), PlayerConnectivity(:,2))) = PlayerConnectivity(:,3);
TeamCheck(sub2ind(size(TeamCheck), PlayerConnectivity(:,2), PlayerConnectivity(:,1))) = PlayerConnectivity(:,3);
if ActiveOnly == 1
    NoP = NoA;
    TeamCheck = TeamCheck(PlayersActive,:);
    TeamCheck = TeamCheck(:,PlayersActive);
end

%% Create random positions
X           = cos(2*pi*(1:NoP)'/NoP)+1;	% Vertex x-coordinates
Y           = sin(2*pi*(1:NoP)'/NoP)+1; % Vertex y-coordinates
Shuffle     = randperm(NoP);            % Randomly shuffle the coordinates
Coords      = [X(Shuffle)  Y(Shuffle)]; % Assign coordinates to each player
maxCanvas   = 2;                        % Maximum plot size
headOffset  = 0.02;                     % Offset to plot the images correctly
redCoords	= Coords./maxCanvas;        % Coordinates scaled with the canvas size

%% Visualize
fig =  figure(1);
axis equal
a = gca;
normalAxis = a.InnerPosition;
gplot(TeamCheck,Coords,'-*')
xlim([0 maxCanvas])
ylim([0 maxCanvas])
hold on
set(gca,'Visible','off')

for i = 1:NoP
    if Coords(i,2) <= 1
        text(Coords(i,1),Coords(i,2)-4*headOffset,Players(PlayersActive(i)).PlayerName,'Interpreter','none',...
            'HorizontalAlignment','center')
    else
        text(Coords(i,1),Coords(i,2)+4*headOffset,Players(PlayersActive(i)).PlayerName,'Interpreter','none',...
            'HorizontalAlignment','center')
    end
end

for i = 1:NoP
    newAxis = [normalAxis(1)+normalAxis(3)*redCoords(i,1)-headOffset...
        normalAxis(2)+normalAxis(4)*redCoords(i,2)-headOffset 2*headOffset 2*headOffset];
    axes('Position',newAxis)
    imshow([Players(PlayersActive(i)).PlayerName '.png'])
end