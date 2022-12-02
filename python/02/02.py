type_map_draw = {
    'A': 'X',
    'B': 'Y',
    'C': 'Z'
}

type_map_win = {
    'A': 'Y',
    'B': 'Z',
    'C': 'X'
}

type_map_loss = {
    'A': 'Z',
    'B': 'X',
    'C': 'Y'
}

def type(a, b):
    if b == 'Y':
        return type_map_draw[a]
    if b == 'X':
        return type_map_loss[a]
    if b == 'Z':
        return type_map_win[a]

def type_points(a):
    match a:
        case 'X': return 1
        case 'Y': return 2
        case 'Z': return 3

def win_points(b):
    if b == 'X':
        return 0
    if b == 'Y':
        return 3
    if b == 'Z':
        return 6

f = open("input.txt", "r", encoding="utf-8-sig")
nums = [n.split() for n in f.read().splitlines()]
score = sum([type_points(type(*n)) + win_points(n[1]) for n in nums])
print(score)