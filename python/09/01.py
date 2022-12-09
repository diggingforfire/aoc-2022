from dataclasses import dataclass, field
@dataclass
class Pos:
    x: int
    y: int
    history: set = field(default_factory=lambda: {(0,0)})

    def is_adjacent_to(self, pos):
        return abs(self.x - pos.x) <= 1 and abs(self.y - pos.y) <= 1

    def follow(self, pos, direction):
        if not self.is_adjacent_to(pos):
            if direction in ['U', 'D']:
                self.x = pos.x
            elif direction in ['L', 'R']:
                self.y = pos.y

            self.step(direction)
            self.history.add((self.x, self.y))

    @property
    def position_count(self):
        return len(self.history)

    def step(self, direction: str):
        match direction:
            case 'R': self.x += 1
            case 'L': self.x -= 1
            case 'U': self.y += 1
            case 'D': self.y -= 1


lines = open("input.txt", "r", encoding="utf-8-sig").read().splitlines()
moves = [l.split() for l in lines]

head = Pos(0, 0)
tail = Pos(0, 0)

for move in moves:
    direction, steps = move
    for i in range(int(steps)):
        head.step(direction)
        tail.follow(head, direction)

print(tail.position_count)