from collections import Counter

input = open("input.txt")
lines = [line.strip() for line in input]

groups = [set()]
for line in lines:
    if line == '':
        groups.append(set())
    else:
        groups[-1].update(set(line))
answer = sum(map(lambda x: len(x), groups))
print(f"Answer 1: {answer}")

group_count = 0
group_list = []
agreed_answers = []
for line in lines:
    if line == '':
        freqs = Counter(group_list)
        group_agreed_list = list(filter(lambda c: freqs[c] == group_count, freqs.keys()))
        group_agreed = len(group_agreed_list)
        agreed_answers.append(group_agreed)
        group_count = 0
        group_list = []
    else:
        group_count += 1
        group_list += list(line)

print(f"Answer 2: {sum(agreed_answers)}")
