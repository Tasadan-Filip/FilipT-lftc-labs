int a;
int b;
int r;
read(a);
read(b);
while(b != 0) {
	r = a % b;
	a = b;
	b = r;
}
write(a);