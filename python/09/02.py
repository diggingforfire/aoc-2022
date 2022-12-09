from dataclasses import dataclass, field
@dataclass
class Pos:
    x: int
    y: int
    id: int
    history: set = field(default_factory=lambda: {(0,0)})

    def is_adjacent_to(self, pos):
        return abs(self.x - pos.x) <= 1 and abs(self.y - pos.y) <= 1
    def __str__(self):
        return self.display_id + f'({self.x},{self.y})'

    @property
    def display_id(self):
        return str(self.id) if 0 < self.id < 9 else 'H' if self.id == 0 else 'T'

    def follow(self, pos, direction):
        if not self.is_adjacent_to(pos):
            if direction in ['U', 'D']:
                diff = pos.y - self.y
                self.y += 1 if diff > 0 else - 1 if diff < 0 else 0
                if self.x != pos.x:
                    self.x += 1 if pos.x - self.x > 0 else - 1

            elif direction in ['L', 'R']:
                diff = pos.x - self.x
                self.x += 1 if diff > 0 else - 1 if diff < 0 else 0
                if self.y != pos.y:
                    self.y += 1 if pos.y - self.y > 0 else - 1

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

knots = [Pos(0, 0, i) for i in range(10)]
head = knots[0]
tails = knots[1:]

def visualize(knots_to_visualize):
    print('')
    max_x = max([k.x for k in knots_to_visualize]) + 2
    max_y = max([k.y for k in knots_to_visualize]) + 2

    for y in reversed(range(-10, max_y + 10)):
        for x in range(-10, max_x + 10):
            found = [k for k in knots if k.x == x and k.y == y]
            if len(found) > 0:
                print(found[0].display_id, end='')
            else:
                print('.', end='')

        print('')

for move in moves:
    direction, steps = move
    for i in range(int(steps)):
        head.step(direction)
        tmp = head
        for tail in tails:
            tail.follow(tmp, direction)
            tmp = tail

#visualize(knots)
print(tails[-1].position_count)