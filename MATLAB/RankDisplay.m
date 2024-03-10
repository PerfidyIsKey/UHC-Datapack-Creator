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
addpath('Data','Functions','Figures')
load('DataS53.mat')

%% Visualization
NoP = size(Players,2);
NoS = size(Seasons,2);
legdata = cell(NoP,1);
for i = 1:NoP
    if ~isnan(Players(i).RankPosition(NoS))
        LineSize = 2*(1 - (Players(i).RankPosition(NoS)-0.5)/NoP);
    else
        LineSize = 0.1;
    end
    Col = rand(1,3);
    legdata{i} = Players(i).PlayerName;
    
    figure(1)
    plot(1:NoS,Players(i).RankHistory,'LineWidth',LineSize,'Color',Col)
    hold on
end
legend(legdata,'location','southoutside','Interpreter','none', "Orientation", "horizontal", ...
    "NumColumns", 5)