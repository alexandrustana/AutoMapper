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

##### Basic usage

First we should create an instance of our `Mapper`, this can be done in the following way
```java
private static Mapper mapper = Mapper.instance();
```
we define it as `static` because it seems like a good ideea to have only one instance of the `Mapper` throughout our entire application. Next we should define the POJO from which we want to transfer the data.
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

Having the POJOs defined and the `Mapper` instantiated the only remaining step before using the mapper is to explicitly add the mapping:
```java
mapper.addMapping(A.class, B.class);
```
Now that we have all set up we can use the mapper in the following way:
```java
A a = new A();
a.setX(1);

B b = mapper.map(a);
```

And *voilà* the `x` attribute from `B` has the same value as the `x` attribute from `A`. 

### Factory

WIP ...
