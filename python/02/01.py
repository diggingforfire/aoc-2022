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

type_points = {
    'X': 1,
    'Y': 2,
    'Z': 3
}

def win_points(a, b):
    if type_map_draw[a] == b:
        return 3
    if type_map_win[a] == b:
        return 6
    return 0

f = open("input.txt", "r", encoding="utf-8-sig")
nums = [n.split() for n in f.read().splitlines()]
score = sum([type_points[n[1]] + win_points(*n) for n in nums])
print(score)