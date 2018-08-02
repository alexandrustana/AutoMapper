auto-mapper
==========
`auto-mapper` is a small utility library written in Java which helps you transfer data between different POJOs which have more or less the same structure.

# Features:
- mapping objects
- cloning objects

# Description:
The features described above are offered by two different entities, `Mapper` and `Factory`, both having a common core.

### Mapper

The mapper can be used for transfering data from one object to another.

#### Basic usage

First we should have an instance of our `Mapper` in our scope, this can be done in the following way
```java
private static Mapper mapper = Mapper.instance();
```
this will make our usage of the `Mapper` less verbose. 

Next we should define the POJOs from which we want to transfer the data.
<table>
<thead>
<tr>
<td>
Source
</td>
<td>
Destination
</td>
</tr>
</thead>
<tbody>
<tr>
<td>
<pre>
<code>
class A {
	private int x;
    public void setX(int a) { this.x = x; }
    public int getX() { return x; }
}
</code>
</pre>
</td>
<td>
<pre>
<code>
class B {
	private int x;
    public void setX(int x) { this.x = x; }
    public int getX() { return x; }
}
</code>
</pre>
</td>
</tr>
</tbody>
</table>

Having the POJOs defined the only remaining step before using the mapper is to explicitly add the mapping we wish
```java
mapper.addMapping(A.class, B.class);
```
Now that we have all set up we can use the mapper in the following way
```java
A a = new A();
a.setX(1);

B b = mapper.map(a);
```

And *voilà* the `x` attribute from `B` has the same value as the `x` attribute from `A`. 
```java
assertEquals(a.getX(), b.getX());
```

### Factory

Using the `Factory` is little easier because you don't have to specify any mapping between classes. 

This being said we could start by having an instance of the factory in the scope
```java
private static Factory factory = Factory.instance();
```
In order to use the factory we must define a class
```java
class C {
	private String y;
    public String getY() { return y; }
    public void setY(String y) { this.y = y; }
}
```
Having the model defined, we can use the factory in the following way
```java
C c = new C();
c.setY("foo");

C cCopy = factory.copy(c);
```
At this point `cCopy` is an exact replica of `c`.
```java
assertEquals(c.getY(), cCopy.getY());
```
