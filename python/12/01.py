import queue
import sys
from dataclasses import dataclass

@dataclass
class Node:
    description: str
    x: int
    y: int
    visited: bool
    distance: int = sys.maxsize
    label: int = 1

    def __str__(self):
        return f'{self.description} {self.elevation} ({self.y},{self.x})'

    def get_neighbours(self, nodes):
        adjacents = { (-1, 0), (1, 0), (0, -1), (0, 1) }
        neighbours = [
            nodes[self.y + y][self.x + x] for x, y in adjacents
            if 0 <= self.x + x < len(nodes[0]) and 0 <= self.y + y < len(nodes) and
            nodes[self.y + y][self.x + x].elevation - self.elevation <= 1]

        return neighbours

    @property
    def elevation(self):
        if self.description == 'S':
            return 1
        if self.description == 'E':
            return 26
        return ord(self.description) - 96

lines = open('input.txt', 'r', encoding='utf-8-sig').read().splitlines()
grid = [[Node(y=y, x=x, description=c, visited=False) for x, c in enumerate(line)] for y, line in enumerate(lines)]
flattened = [cell for row in grid for cell in row]

start = [n for n in flattened if n.description == 'S'][0]
start.distance = 0
current = start
end = None

q = queue.Queue()
q.put(start)
start.visited = True
while not q.empty():
    current = q.get()
    if current.description == 'E':
        end = current
        break

    for neighbour in current.get_neighbours(grid):
        if not neighbour.visited:
            neighbour.visited = True
            distance = neighbour.label + current.distance
            neighbour.distance = min(distance, neighbour.distance)
            q.put(neighbour)

print(end.distance)