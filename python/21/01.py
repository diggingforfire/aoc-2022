get_value = lambda name, jobs: \
    int(jobs[name]) if len(jobs[name]) <= 4 else \
    eval(f'get_value(jobs[name][0:4], jobs) {jobs[name][5]} get_value(jobs[name][7:11], jobs)')

lines = open('input.txt', 'r', encoding='utf-8-sig').read().splitlines()
jobs = dict((part[0].strip(), part[1].strip()) for part in [line.split(':') for line in lines])
print(int(get_value('root', jobs)))