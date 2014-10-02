# Labb 2 Teoriuppgifter
Några av teoriuppgifternas lösningar.

## Matris med orden LABD och BLAD:

   |   | L | A | B | D
---|---|---|---|---|---
   | 0 | 1 | 2 | 3 | 4
 B | 1 | 2 | 3 | 2 | 3
 L | 2 | 1 | 2 | 3 | 4
 A | 3 | 2 | 1 | 2 | 3
 D | 4 | 3 | 2 | 3 | 2

## DynProg
Detta blev en lite mer kodig pseudokod, men det är fortfarande pseudokod

```
m(s1[1..i], s2[1..j]) {
  matris d[0..i][0..j]
  for u from 0 to i {
    d[u][0] = u 
  }
  for n from 0 to j {
    d[0][j] = u 
  }
  for a from 1 to i {
    for b from 1 to j {
      if s1[a] == s2[b] then
        d[a][b] = d[a-1][b-1]
      else {
        d[a][b] = min(
          d[a-1][b-1] + {if s1[a] == s2[b] then 0 else 1}
          d[a-1][b] +1
          d[a][b-1] +1  
        )
      }
    }
  }
}
```
