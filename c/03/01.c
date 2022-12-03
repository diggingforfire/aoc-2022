#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFFER_LENGTH 50

struct rucksack {
    char left[BUFFER_LENGTH / 2];
    char right[BUFFER_LENGTH / 2];
    struct rucksack *next;
};
  
struct rucksack *get_input()
{
    char buffer[BUFFER_LENGTH];
    FILE *fp = fopen("input.txt", "r");
    struct rucksack *tmp = NULL;

    while (fgets(buffer, BUFFER_LENGTH, fp) != NULL)
    {
        struct rucksack *r = (struct rucksack *) malloc(sizeof(struct rucksack));
        r->next = NULL;
        
        if (tmp == NULL)
        {
            tmp = r;
        }
        else 
        {
            r->next = tmp;
            tmp = r;
        }

        buffer[strcspn(buffer, "\n")] = 0;
        int half = strlen(buffer) / 2;

        strncpy(r->left, buffer, half);
        r->left[half] = '\0';
        strncpy(r->right, buffer + half, half);
        r->right[half] = '\0';
    }

    fclose(fp);

    return tmp;
}

char intersect(char *left, char *right) 
{
    for (char *tmp = left; *tmp != '\0'; tmp++) 
    {
        for (char *tmp2 = right; *tmp2 != '\0'; tmp2++) 
        {
            if (*tmp == *tmp2) 
            {
                return *tmp;
            }
        }
    }   
}

int main(int argc, char *argv[])
{
    struct rucksack *rucksacks = get_input();
    struct rucksack *next = rucksacks;
    struct rucksack *tmp = NULL;
    int sum = 0;

    while (next != NULL)
    {
        char intersection = intersect(next->left, next->right);
        sum += intersection >= 97 ? intersection - 96 : intersection - 38;
        tmp = next;
        next = next->next;
        free(tmp);
    }

    printf("%i", sum);
}