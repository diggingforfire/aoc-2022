f = open("input.txt", "r", encoding="utf-8-sig")
rucksacks = [(line[:len(line)//2], line[len(line)//2:]) for line in f.read().splitlines()]
in_both = [list(set(r[0]) & set(r[1]))[0] for r in rucksacks]
ordinals = [ord(o) for o in in_both]
priorities = [o - 96 if o >= 97 else o - 38 for o in ordinals]
print(sum(priorities))