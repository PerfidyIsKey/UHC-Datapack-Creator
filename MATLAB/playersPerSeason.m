clear
close all

addpath("Functions", "Documents", "Data")

% Load data
Players = struct;
load("DataS57.mat")

%% Create data
seasonNumber = length(Seasons);                 % Amount of played seasons

% Pull data from structure
playerNumber    = zeros(1, seasonNumber);       % Preallocation
seasonDate      = strings(1, seasonNumber);     % Preallocation
for i = 1:seasonNumber
    playerNumber(i) = Seasons(i).Players;       % Number of players during each season
    seasonDate(i)   = string(Seasons(i).Date);  % Date of the season
end

%% Visualization
xDisplayAmount  = ceil(seasonNumber/10);
xTicks          = ceil(linspace(1, seasonNumber, xDisplayAmount));
xTickLabels     = seasonDate(xTicks);

figure("Name", "Players per season")
plot(playerNumber, "Color", "b", "LineWidth", 1.5)
grid minor
xlim([1, seasonNumber])
xlabel("Date")
ylabel("Number of players")
title("Change in number of players per season over time")

ax = gca;
ax.XTick = xTicks;
ax.XTickLabel = xTickLabels;
ax.FontWeight	= 'bold';
ax.FontSize = 12;