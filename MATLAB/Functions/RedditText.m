function string = RedditText(Players,Seasons)

NN = size(Players,2);   % Number of players in database
N = size(Seasons,2);    % Number of seasons in database

% Preallocation
text = cell(NN,1);
PlayerOrder = zeros(NN,1);
for i = 1:NN
    % Loop over rank position of current season
    PlayerOrder(i) = Players(i).RankPosition(end);
end

%% Fill rank entries
for i = 1:NN
    
    j = find(PlayerOrder == i); % Find all players with this rank position
    jnot = j;
    currentAmount = size(j,1);  % Amount of players with this rank

    % for UwU = 1:currentAmount
    if ~isempty(j)  % If this player has a rank
        for iii = 1:currentAmount
            if iii > 1
                i = i + 1;
            end
            j = jnot(iii);

            % Personal bests
            if Players(j).Rank == max(Players(j).RankHistory)   % Check if their current rank is a personal best
                BestRank = " **- PB!**";
            else
                BestRank = "";
            end
            
            if Players(j).RankPosition(N) == min(Players(j).RankPosition)   % Check if their current position is a personal best
                BestRankPosition = " **- PB!**";
            else
                BestRankPosition = "";
            end
            
            % Best performance score
            BestPerformanceAmount = find(Players(j).PerformanceScore == max(Players(j).PerformanceScore));
            PerformanceSeason = "";
            for ii = 1:size(BestPerformanceAmount,1)
                BestPerformance = Seasons(BestPerformanceAmount(ii)).Season;
                if ii ~= size(BestPerformanceAmount,1)
                    % Multiple best performances
                    PerformanceSeason = PerformanceSeason + BestPerformance + ", ";
                else
                    PerformanceSeason = PerformanceSeason + BestPerformance;
                end
            end
            
            % Kills
            if max(Players(j).Kills) ~= 0
                BestKillAmount = find(Players(j).Kills == max(Players(j).Kills));
                KillSeason = " (in UHC ";
                for ii = 1:size(BestKillAmount,1)
                    BestKills = Seasons(BestKillAmount(ii)).Season;
                    if ii ~= size(BestKillAmount,1)
                        % Multiple best number of kills
                        KillSeason = KillSeason + BestKills + ", ";
                    else
                        KillSeason = KillSeason + BestKills + ")";
                    end
                end
            else
                KillSeason = '';
            end
            
            % Rank and position change
            if find(~isnan(Players(j).RankPosition)) > 1 | ~isnan(Players(j).RankPosition(1))
                if isnan(Players(j).RankPosition(end-1) - Players(j).RankPosition(end))
                    % New player
                    RankChange = "NEW";
                    RankChangeNum = "NEW";
                else
                    % Change in rank compared to previous season
                    RankChange = num2str(Players(j).RankPosition(end-1) - Players(j).RankPosition(end),'%+i');
                    RankChangeNum = num2str(int8(Players(j).Rank - Players(j).RankHistory(end-1)),'%+d');
                end
            else
                % New tournament
                RankChange = "NEW";
                RankChangeNum = "NEW";
            end
                
            % Best ranking
            if max(Players(j).RankHistory) == 0
                % Rank of 0
                BestSeason  = find(~isnan(Players(j).RankPosition),1);
            else
                % Other ranks
                BestSeason  = Players(j).RankHistory == max(Players(j).RankHistory);
            end

            % Current ranking
            if Players(j).RankHistory(end) == 0
                currentRank = Players(j).RankPosition(end) + (iii - 1);
            else
                currentRank = Players(j).RankPosition(end);
            end
            
            if ~isnan(Players(j).Rank)
                text{i} = [
                    "**" + num2str(currentRank) + " (" + RankChange + ") " + strrep(Players(j).PlayerName,'_','\_') + "**" + BestRankPosition
                    "Rank: " + num2str(Players(j).Rank,'%.0f') + " (" + RankChangeNum + ") " + BestRank
                    "Number of games: " + num2str(sum(Players(j).Participation))
                    "Total number of wins: " + num2str(sum(Players(j).Winner))
                    "Best performance: " + num2str(max(Players(j).PerformanceScore),'%.2f') + " (in UHC " + PerformanceSeason + ")"
                    "Total number of kills: " + num2str(sum(Players(j).Kills))
                    "Average number of kills: " + num2str(sum(Players(j).Kills)/sum(Players(j).Participation),'%.2f')
                    "Highest number of kills in a game: " + num2str(max(Players(j).Kills)) + KillSeason
                    "Mean Survivalrate: " + num2str(Players(j).SurvivalRate,'%.2f')
                    "Peak ranking score: " + num2str(max(Players(j).RankHistory),'%.0f') + " (after UHC " + Seasons(BestSeason).Season + ")"
                    "Highest rank: " + iptnum2ordinal(min(Players(j).RankPosition)) + " (x" + num2str(size(find(Players(j).RankPosition == min(Players(j).RankPosition)),1)) + ", last achieved in UHC " + Seasons(find(Players(j).RankPosition == min(Players(j).RankPosition,[],'omitnan'),1,'last')).Season + ")"
                    "Last UHC season: " + num2str(Seasons(find(Players(j).Participation == 1,1,'last')).Season) + " (" + datestr(Seasons(find(Players(j).Participation == 1,1,'last')).Date, 24) + ")"
                    "&nbsp;"
                    ];
            end
            ClassifiedPlayers = i;
        end
    else
        if iii == 1
            ClassifiedPlayers = i - 1;
            text = text(~cellfun('isempty',text));
        end
        % break
    % end
    end
end

StringEntries = size(text{1,1});
string = "";
j = 1;
for ii = 1:ClassifiedPlayers
    for i = 1:StringEntries
        
        char = text{ii,1}(i);
        try
        string(j) = convertCharsToStrings(char);
        catch
            fprintf("j = %3.0f\n", j)
        end
        j = j + 1;
    end
end

end

