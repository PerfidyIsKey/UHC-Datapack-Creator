function Players = RankDetermine(Players,Seasons)

NoS = size(Seasons,2);
NoP = size(Players,2);

Weight = zeros(NoS,1);
SurvivalRate = NaN(NoS,NoP);
PerformanceScore = NaN(NoS,NoP);
WeightedPerformance = zeros(NoS,NoP);
for i = 1:NoS
    Months = between(Seasons(i).Date,Seasons(NoS).Date);
    [Years,Months,Days] = split(Months,{'years','months','days'});
    Months = Months + 12*Years;
    if Days > 15
        Months = Months + 1;
    end
    Weight(i) = (48 - Months)/48;
    if Weight(i) < 0
        Weight(i) = 0;
    end
    TotKills = zeros(NoP,1);
    for k = 1:NoP
        TotKills(k) = Players(k).Kills(i);
    end
    SeasonKills = sum(TotKills);
    
    for j = 1:NoP
        if ~isnan(Players(j).Position(i))
            SurvivalRate(i,j) = (Seasons(i).Players - Players(j).Position(i)) /...
                (Seasons(i).Players - 1);
            Performance = (Players(j).Kills(i) + Players(j).Winner(i)*SurvivalRate(i,j))/...
                (SeasonKills + Seasons(i).TeamAmount);
            PerformanceScore(i,j) = Performance*0.5*Seasons(i).Players + SurvivalRate(i,j);
            WeightedPerformance(i,j) = Weight(i)*PerformanceScore(i,j);
        end
    end
end

for ii = 1:NoP
    Players(ii).SurvivalRate = nanmean(SurvivalRate(:,ii));
    Players(ii).PerformanceScore = PerformanceScore(:,ii);
    if sum(Weight.*Players(ii).Participation(1:NoS)) ~= 0
    Players(ii).Rank = 100*sum(WeightedPerformance(:,ii))/...
        (sum(Weight.*Players(ii).Participation(1:NoS)) + 2.5);
    else
        Players(ii).Rank = NaN;
    end
end

RankSort = zeros(NoP,1);
for i = 1:NoP
    RankSort(i) = Players(i).Rank;
    Players(i).RankHistory(NoS,:) = Players(i).Rank;
end
RankSort(isnan(RankSort)) = [];
RankSort = sort(RankSort,'descend');
NoQ = size(RankSort,1);

for i = 1:NoP
    for ii = 1:NoQ
        if RankSort(ii) == Players(i).Rank
            Players(i).RankPosition(NoS,:) = ii;
            break
        elseif ii == NoQ
            Players(i).RankPosition(NoS,:) = NaN;
        end
    end
end
end

