function newTeams = eliminateEmptyTeams(oldTeams, teamNumber)

% Eliminate empty teams
excludedTeams   = find(~ismember(1:teamNumber, oldTeams));

if ~isempty(excludedTeams)
    % Start process if empty teams are found
    excludedCopy    = excludedTeams;
    count           = 1;    % Initiate counter
    for i = 1:teamNumber
        if i == excludedTeams(count)
            % Shift down teams
            teamsChange = oldTeams > excludedCopy(count);    % Indicate teams to change
            oldTeams(teamsChange) = oldTeams(teamsChange) - 1;

            % Update copy of empty teams
            excludedCopy = excludedCopy - 1;

            % Update counter
            count = count + 1;
        end

        % Break loop if all teams have been found
        if count > length(excludedTeams)
            break
        end
    end
end

% Assign new teams
newTeams = oldTeams;
end