function string = RedditText(Players,Seasons)

NN = size(Players,2);
N = size(Seasons,2);

text = cell(NN,1);
PlayerOrder = zeros(NN,1);
for i = 1:NN
    PlayerOrder(i) = Players(i).RankPosition(end);
end
for i = 1:NN
    
    j = find(PlayerOrder == i);
    jnot = j;
    NoJ = size(j,1);
    if ~isempty(j)
        for iii = 1:NoJ
            if iii > 1
                i = i + 1;
            end
            j = jnot(iii);
            if Players(j).Rank == max(Players(j).RankHistory)
                BestRank = ' **- PB!**';
            else
                BestRank = '';
            end
            
            if Players(j).RankPosition(N) == min(Players(j).RankPosition)
                BestRankPosition = ' **- PB!**';
            else
                BestRankPosition = '';
            end
            
            BestPerformanceAmount = find(Players(j).PerformanceScore == max(Players(j).PerformanceScore));
            PerformanceSeason = '';
            for ii = 1:size(BestPerformanceAmount,1)
                BestPerformance = Seasons(BestPerformanceAmount(ii)).Season;
                if ii ~= size(BestPerformanceAmount,1)
                    PerformanceSeason = [PerformanceSeason BestPerformance ', '];
                else
                    PerformanceSeason = [PerformanceSeason BestPerformance];
                end
            end
            
            if max(Players(j).Kills) ~= 0
                BestKillAmount = find(Players(j).Kills == max(Players(j).Kills));
                KillSeason = ' (in UHC ';
                for ii = 1:size(BestKillAmount,1)
                    BestKills = Seasons(BestKillAmount(ii)).Season;
                    if ii ~= size(BestKillAmount,1)
                        KillSeason = [KillSeason BestKills ', '];
                    else
                        KillSeason = [KillSeason BestKills ')'];
                    end
                end
            else
                KillSeason = '';
            end
            
            if isnan(Players(j).RankPosition(end-1) - Players(j).RankPosition(end))
                RankChange = 'NEW';
            else
                RankChange = num2str(Players(j).RankPosition(end-1) - Players(j).RankPosition(end),'%+i');
            end
            
            if max(uint8(Players(j).RankHistory)) == 0
                BestSeason  = find(~isnan(Players(j).RankPosition),1);
            else
                BestSeason  = Players(j).RankHistory == max(Players(j).RankHistory);
            end
            
            if ~isnan(Players(j).Rank)
                text{i} = {['**' num2str(Players(j).RankPosition(end)) ' (' RankChange ') ' strrep(Players(j).PlayerName,'_','\_') '**' BestRankPosition]
                    ['Rank: ' num2str(uint8(Players(j).Rank),'%u') ' (' num2str(int8(Players(j).Rank - Players(j).RankHistory(end-1)),'%+d') ') ' BestRank]
                    ['Number of games: ' num2str(sum(Players(j).Participation))]
                    ['Total number of wins: ' num2str(sum(Players(j).Winner))]
                    ['Best performance: ' num2str(max(Players(j).PerformanceScore),'%.2f') ' (in UHC ' PerformanceSeason ')']
                    ['Total number of kills: ' num2str(sum(Players(j).Kills))]
                    ['Average number of kills: ' num2str(sum(Players(j).Kills)/sum(Players(j).Participation),'%.2f')]
                    ['Highest number of kills in a game: ' num2str(max(Players(j).Kills)) KillSeason]
                    ['Mean Survivalrate: ' num2str(Players(j).SurvivalRate,'%.2f')]
                    ['Peak ranking score: ' num2str(max(uint8(Players(j).RankHistory)),'%u') ' (after UHC ' Seasons(BestSeason).Season ')']
                    ['Highest rank: ' iptnum2ordinal(min(Players(j).RankPosition)) ' (x' num2str(size(find(Players(j).RankPosition == min(Players(j).RankPosition)),1)) ', last achieved in UHC ' Seasons(find(Players(j).RankPosition == min(Players(j).RankPosition,[],'omitnan'),1,'last')).Season ')']
                    ['Last UHC season: ' num2str(Seasons(find(Players(j).Participation == 1,1,'last')).Season) ' (' datestr(Seasons(find(Players(j).Participation == 1,1,'last')).Date) ')']
                    '&nbsp;'};
            end
            ClassifiedPlayers = i;
        end
    else
        if iii == 1
            ClassifiedPlayers = i - 1;
            text = text(~cellfun('isempty',text));
        end
        break
    end
end

StringEntries = size(text{1,1});
string = "";
j = 1;
for ii = 1:ClassifiedPlayers
    for i = 1:StringEntries
        
        char = text{ii,1}{i};
        string(j) = convertCharsToStrings(char);
        j = j + 1;
    end
end

end

