def get_value(name, jobs):
    job = jobs[name]
    if len(job) <= 4:
        return int(job)
    else:
        return eval(f'get_value(job[0:4], jobs) {job[5]} get_value(job[7:11], jobs)')

def get_root(name, jobs):
    if name in jobs['root']:
        return name
    return get_root([(key, jobs[key]) for key in jobs.keys() if name in jobs[key]][0][0], jobs)

# https://towardsdatascience.com/the-most-efficient-way-to-solve-any-linear-equation-in-three-lines-of-code-bb8f66f1b463
def solve_linear(equation,var='x'):
    expression = equation.replace("=", "-(")+")"
    grouped = eval(expression.replace(var, '1j'))
    return -grouped.real/grouped.imag

lines = open('input.txt', 'r', encoding='utf-8-sig').read().splitlines()
jobs = dict((part[0].strip(), part[1].strip()) for part in [line.split(':') for line in lines])

job_that_leads_to_humn = get_root('humn', jobs)
value_of_the_other_job = get_value(jobs['root'].replace(job_that_leads_to_humn, '').strip().strip('+').strip(), jobs)

parent = [(key, jobs[key]) for key in jobs.keys() if 'humn' in jobs[key]][0]

equation = 'x'
lastName = 'humn'

while True:
    name, job = parent
    equation = f'({job.replace(lastName, equation)})'
    parent = [(key, jobs[key]) for key in jobs.keys() if name in jobs[key]][0]
    if parent[0] == 'root':
        break
    lastName = name

jobs['humn'] = 'x'
jobs[job_that_leads_to_humn] = str(value_of_the_other_job)

for job in jobs:
    if job != 'x' and job in equation:
        val = str(get_value(job, jobs))
        equation = equation.replace(job, val)

equation = f'{equation} = {value_of_the_other_job}'
print(int(solve_linear(equation)))