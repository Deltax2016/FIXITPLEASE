#include "stdafx.h"
#include <sstream>
#include <iostream>
#include <string>
#include <algorithm>

using namespace std;

stringstream s3;
string s1,s4, s2 = "9223372036954775807";
long long n;
string s5 = "9223372036954775808";
bool f1 = 0;


int main()
{
	cin >> s1;
	if ((s1[0] != '-') && (s1.length() < 20) && (((s1 <= s2) && (s1.length() <= s2.length())) || (s2.length() > s1.length())))
	{
			s3 << s1;
			s3 >> n;
			if (n <= 127 )
			{
				cout << "byte";
				f1 = 1;
			}
			if ((n <= 32767) && (f1 == 0))
			{
				cout << "short";
				f1 = 1;
			}
			if ((n <= 2147483647) && (f1 == 0))
			{
				cout << "int";
				f1 = 1;
			}
			if ((n <= 9223372036954775807) && (f1 == 0))
			{
				cout << "long";
				f1 = 1;
			}
	}
	if (s1[0] == '-')
	{
		s1 = s1.substr(1, s1.length()-1);
		if ((s1.length() < 20) && (((s1 <= s2) && (s1.length() <= s5.length())) || (s5.length() > s1.length())))
		{
			if (s1 == "9223372036954775808")
			{
				cout << "long";
			}
			else
			{
				s3 << s1;
				s3 >> n;
				cout << n << endl;
				if (n <= 128)
				{
					cout << "byte";
					f1 = 1;
				}
				if ((n <= 32768) && (f1 == 0))
				{
					cout << "short";
					f1 = 1;
				}
				if ((n <= 2147483648) && (f1 == 0))
				{
					cout << "int";
					f1 = 1;
				}
				if ((n <= 9223372036954775807) && (f1 == 0))
				{
					cout << "long";
					f1 = 1;
				}
			}
		}
	}
	if (f1 == 0)
	{
		cout << "BigInteger";
	}
	return 0;
}

