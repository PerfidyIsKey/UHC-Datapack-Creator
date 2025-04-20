clear
close all

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% UltraHardCore Score Statistics
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Load data
addpath('Data','Functions','Documents')
load('DataS58.mat')

%% Boxplot statistics
NoP = size(Players,2);

g1          = 1;            % Initialization
for i = 1:NoP
    if Players(i).Active == 1
    SeasonAmount	= nnz(~isnan(Players(i).RankHistory));  % Amount of laps per run
    
    %%% Boxplot data
    g2                  = g1 + (SeasonAmount-1);   % Lap index
    g(g1:g2,:)          = repmat({Players(i).PlayerName},SeasonAmount,1); % String collection
    Rank(g1:g2,:)       = Players(i).RankHistory(~isnan(Players(i).RankHistory));              % Lap time collection
    g1                  = g2 + 1;                       % Lap index
    end
end

figure(1)
boxplot(Rank,g)
ylabel('Rank [-]','FontSize',12,'FontWeight','bold')
ax = gca; ax.FontWeight	= 'bold'; ax.FontSize = 12;
MeanRank = mean(Rank);