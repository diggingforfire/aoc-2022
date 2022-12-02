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

type_points = {
    'X': 1,
    'Y': 2,
    'Z': 3
}

win_points = {
    'X': 0,
    'Y': 3,
    'Z': 6
}

def type(a, b):
    match b:
        case 'Y': return type_map_draw[a]
        case 'X': return type_map_loss[a]
        case 'Z': return type_map_win[a]

f = open("input.txt", "r", encoding="utf-8-sig")
nums = [n.split() for n in f.read().splitlines()]
score = sum([type_points[type(*n)] + win_points[n[1]] for n in nums])
print(score)