f = open("input.txt", "r", encoding="utf-8-sig")
rucksacks = f.read().splitlines()
groups = [rucksacks[i:i+3] for i in range(0, len(rucksacks), 3)]
badges = [list(set(r[0]) & set(r[1]) & set(r[2]))[0] for r in groups]
ordinals = [ord(o) for o in badges]
priorities = [o - 96 if o >= 97 else o - 38 for o in ordinals]
print(sum(priorities))