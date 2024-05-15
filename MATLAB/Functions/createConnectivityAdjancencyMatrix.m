function A = createConnectivityAdjancencyMatrix(playerNumber, playerConnectivity, ...
    participantIndex)

%%% Define adjacency matrix
A = zeros(playerNumber);
A(sub2ind(size(A), playerConnectivity(:, 1), playerConnectivity(:, 2))) = playerConnectivity(:, 3);
A(sub2ind(size(A), playerConnectivity(:, 2), playerConnectivity(:, 1))) = playerConnectivity(:, 3);
A = A(participantIndex, :);
A = A(:, participantIndex);
end