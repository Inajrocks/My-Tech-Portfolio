In cryptography, modular arithmetic only uses integers that to find the remainder by dividing one number by another. 
It's usually associated with prime numbers which ensures division leads to predictable behavior within a program. This is important for modern cryptographic algorithms, which rely on these operations to secure data. 
Calculators like this one are used within many applications, specifically within in cryptography for public-key key generation, checksums for data integrity, and  error detection.
**Created to show understanding and application during Introduction to Cryptography – D334**



(a x b) mod m = ((a mod m) x (b mod m)) mod m

Example:

(12 x 13) % 5
= ((12 % 5) x (13 % 5)) % 5
= (2 x 3) % 5
= 6 % 5
= 1

#include<bits/stdc++.h>
#include<iostream>
​
using namespace std;
//function that 
