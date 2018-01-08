#### 集合

* Iterable<T> interface: 可迭代接口.实现该接口的对象允许for-each循环
    * Iterator<T> iterator():返回一个迭代器
    * default void forEach(Consumer<? super T> action): Java8. 使用action.accept()消费每个元素,
        直到全部消费或抛出异常.
        >
            例如,不过ArrayList重写了该方法.
            new ArrayList<>().forEach(item->{
            			System.out.println(item);
            		});
        >
    * default Spliterator<T> spliterator(): 创建Spliterator(可分割迭代器).一个fork/join的并行处理的迭代器,此处不详细展开.
* Iterator<E> interface: 迭代器接口,所有迭代器需要实现该接口.
    * boolean hasNext(): 判断是否有下一个元素
    * E next(): 返回下一个元素.如果没有.则抛出NoSuchElementException异常
    * default void remove(): 删除元素,在底层collection中.默认实现是抛出异常
    * default void forEachRemaining(Consumer<? super E> action):  对迭代器剩下的所有元素进行相同操作,直到完成或抛出异常.
* 如下是一个自定义迭代器的例子:
> https://github.com/BrightStarry/Pattern/blob/master/src/main/iterator/com/zx/a/IteratorTest.java    
        
* Collection<E> interface: 所有集合的父类
    * int size(): 返回集合元素数量.如果大于Integer.MAX_VALUE,则返回Integer.MAX_VALUE
    * boolean isEmpty():集合是否是空的
    * boolean contains(Object o):如果该集合包含指定元素,返回true, 更准确的说,如果这个集合至少包含了一个该指定元素.
    * Iterator<E> iterator():返回一个迭代器
    * Object[] toArray():将所有元素转为数组,必须是新数组(没有其他引用)
    *  <T> T[] toArray(T[] a):如果传入的数组size>集合size,将所有元素放入该数组.否则返回一个新的集合数组.
        可以这样使用String[] y = x.toArray(new String[0]);相当于Object[] toArray()方法指定了返回类型
    * boolean add(E e): 新增元素,可根据集合自身,做一些例如非空/不可重复限制
    * boolean remove(Object o): 删除一个元素.
    * boolean containsAll(Collection<?> c):是否包含指定集合中的所有元素.
    * boolean addAll(Collection<? extends E> c):增加指定集合的所有元素到该集合
    * boolean removeAll(Collection<?> c):删除集合中和指定集合中的所有元素相同的元素
    * boolean retainAll(Collection<?> c): 在集合中,只保留指定集合中存在的元素.删除其他所有元素.
    * void clear():删除集合中的所有元素
    * boolean equals(Object o): 比较集合和另一集合是否相同.例如AbstractList中的实现是,按顺序比较
        两个集合中的每个元素是否相同.
    * int hashCode():返回集合的hashcode.例如AbstractList中的实现是,累加每个元素的hashcode.
    * default boolean removeIf(Predicate<? super E> filter)  : Java8接口默认实现方法.
            删除满足条件(filter.test()为true)的所有元素. filter可以理解为一个lambda表达式,返回boolean.
    * default Spliterator<E> spliterator() : 返回Spliterator<E>
    * default Stream<E> stream():返回  Stream<E> 
    * default Stream<E> parallelStream(): 返回Stream<E> ,并发的.

* AbstractCollection<E> abstract: 抽象集合类.集合接口的最小实现.
要实现一个不可修改的集合,只需要继承该类,然后实现iterator()和size()即可.(返回的迭代器必须实现hasNext()和next()方法)
要实现可修改的集合,必须重写add()方法,默认是抛出UnsupportedOperationException.并且返回的迭代器必须实现remove()方法
    * boolean isEmpty(): 实现:  return size() == 0;
    * boolean contains(Object o): 遍历集合每个元素,用equals和o比较.
    * Object[] toArray(): 遍历,将每个元素放入数组,最后使用 return Arrays.copyOf(r, i); 返回一个全新的数组. i为元素个数.
    * int MAX_ARRAY_SIZE : 对toArray()方法作的最大数组长度限制,值为Integer.MAX_VALUE - 8,-8是因为有些VM在数组头保留了一些信息.
    * <T> T[] toArray(T[] a): 类似,用了Array.newInstance()/Arrays.copyOf/System.arraycopy()方法
        >
            toArray方法可能会有比预期更多的元素(它是预先调用size()结果作为数组大小的.如果不符,应该是间隔间其他线程增加了元素),
            那么它在返回前会尽力保证和当前数据一致,所以再次判断了下,如果超出预期,则调用finishToArray()方法继续增加.
            并会用hugeCapacity()方法.最大限制其为Integer.MAX_VALUE,如果超出(Integer.MAX_VALUE + 1),则由0x7fffffff变为0x80000000,    
            最高位变为1,表示负数,所以.它判断其容量<0后,抛出OutOfMemoryError
        >
    * boolean add(E e):直接抛出UnsupportedOperationException
    * boolean remove(Object o): 迭代器遍历.依次比较,删除所有相同元素
    * boolean containsAll(Collection<?> c): 遍历,循环调用contains(Object o)方法.时间复杂度O(n^2).
    * boolean addAll(Collection<? extends E> c):遍历,循环调用add(E e).
    * boolean removeAll(Collection<?> c):遍历,循环调用remove(Object o),时间复杂度O(n^2).
    * boolean retainAll(Collection<?> c):遍历,循环调用c.contains(),时间复杂度O(n^2).
    * void clear(): 迭代,循环调用remove()方法.大多数实现会重写该方法(应该是将一个内部数组 == null)
    * String toString() :一个我们常见的toString实现.
   
* List<E> interface: 一个有序集合,可通过整数索引查询元素.   
提供了一个特殊的迭代器ListIterator.允许元素的插入和替换.还允许双向访问Iterator接口提供的迭代器
    * default void replaceAll(UnaryOperator<E> operator) : 用运算操作的值替换每个元素
        >
            默认实现
            final ListIterator<E> li = this.listIterator();
                    while (li.hasNext()) {
                        li.set(operator.apply(li.next()));
                    }
            如果list实现不支持set操作,在操作第一个元素时将抛出UnsupportedOperationException
        >   
    * default void sort(Comparator<? super E> c): 排序.如果指定的比较器是空的,则元素必须实现Comparable接口
    * int indexOf(Object o): 返回元素最小的索引(第一次出现),没有则返回-1
    * int lastIndexOf(Object o): 返回元素最大的索引(最后一次出现),没有则返回-1
    * ListIterator<E> listIterator():返回特殊的list迭代器
    * ListIterator<E> listIterator(int index): 返回以指定元素开始的迭代器.
    * List<E> subList(int fromIndex, int toIndex):返回list指定索引间的list视图.不是新集合. 
        > 如果两个索引相同,则列表为空.  还有, 索引取头不取尾. 也就是 1,2 只会删除第2个元素(第二个元素索引为1)
        > 这样可以直接删除list中指定索引间的元素 list.subList(from, to).clear();
* ListIterator<E> interface: List接口特殊的迭代器,其实实现在每个list子类中 
    * 该接口实现了Iterator<E>接口
    * boolean hasPrevious(): 相反方向判断的hasNext(); 就是是否有前一个元素
    * E previous(): 返回前一个元素
    * int nextIndex(): 返回下一次调用next()的对象的索引,如果已经在最末尾,则返回list长度
    * int previousIndex(): 返回下一次调用previous()的对象的索引,如果已经在开头,则返回-1
    * void remove(): 删除当前元素,必须在调用next或previous后调用
    * void set(E e): 重新set当前元素的值.必须在调用next或previous后调用
    * void add(E e): 增加元素在当前位置.

* AbstractList<E> abstract extends AbstractCollection<E> implements List<E> : 抽象list;  
最小化的实现了list接口,数据由能是随机读写的数据结构(例如数组)存储.  
如果需要顺序读写,AbstractSequentialList<E>抽象类应该比该类优先更好使.
    * add(int index, E element) : 在指定索引增加元素的方法,该类的默认实现是抛异常. add(E e)的实现也依靠它,index参数为size()
    * E get(int index): 从指定索引获取数据...
    
#### 杂
我想了下,这一个个方法看过来似乎毫无软用..我还是直接看类的具体实现把..从大局上看.

#### List相关(包含Queue/Deque)
* AbstractList<E>抽象的随机读写list类.
    * Itr:实现了迭代器接口
    >
        抽象类中有一个modCount字段,保存该类修改操作次数.
        Itr中,expectedModCount变量保存当前迭代器对该类的修改操作次数.如果和modCount不符合,则抛出ConcurrentModificationException
        其主要迭代功能,依靠维护一个cursor索引变量,next()方法调用get(cursor)方法获取下一元素.
        又维护lastRet变量,保存最近一个通过next()方法获取的对象的索引,删除时调用remove(lastRet)方法.
    >
    * ListItr:实现list特殊的迭代器接口
    >
        可通过给cursor变量附初始值指定起始元素.
        其他的和Itr类似,只不过可以通过cursor 加减,来获取前一个或后一个元素.
        没有删除功能.通过add(i, e)和set()来修改/增加元素
    >
    * class SubList<E> extends AbstractList<E>: 该类维护了调用subList()方法后,从原list上切割的一段范围的list.
        但其实现不过是维护了原list.并限定了下标,使其无法操作范围外的元素而已.
    * RandomAccessSubList: 继承了SubList,实现RandomAccess接口,其他没有作任何扩展.只是为了在其他算法调用list时,能知道它是能随机读写的

* AbstractSequentialList<E> 抽象的顺序读写list类.
>
    该类的基本操作全部通过ListIterator迭代器完成.
    例如增加到指定索引. 通过返回一个指定索引开始的ListIterator.然后调用迭代器的add()方法完成增加.
    并且默认实现默认的ListIterator.
>

* Queue<E>接口: 
>
    add:增加元素; offer:获取元素; 
    remove:删除并返回首元素,为空抛异常; poll:删除并返回首元素,为空返回null;
    element: 查看但不删除首元素,为空抛异常; peek:查看但不删除首元素,为空返回null;
>

* Deque<E>接口,继承Queue<E>接口:
>
    addFirst   addLast  offerFirst offerLast
    反正就这类头部尾部元素各种操作的方法.不赘述
>
    
*  ArrayList<E> : extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable    
    * 综述
    >
        一个底层为Object[],然后通过System.arraycopy()等方法.操作扩容数组的一个集合.
        除了java8的方法外,还有SubList类,引用了自身,进行操作,稍显繁琐,其他没有什么特别的地方
    >
    * 属性介绍
    >
        DEFAULT_CAPACITY = 10:初始容量 
        Object[] EMPTY_ELEMENTDATA = {} : 空list的表示,是真正的初始容量为0的空list
        Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {}:空list的表示,是初始化的容量为10的空list
        elementData : 真正的元素数据,通常会比size大上一些,用来预扩容
        size: 真正存在的元素个数,可能小于elementData.length,可以使用trimToSize()方法去掉末尾空元素
            也就是说,调用后,elementData会删除预扩容的所有空元素.
        int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8 : 分配数组的最大大小.8位预留给特殊的VM
        
        之前一直没考虑过为什么很多java库类都要为空专门定义一个对象.现在明白了.就是为了不重复的创建空对象.
    > 
    * 扩容
    >
         ensureCapacity(int minCapacity) : 外部调用主动扩容,DEFAULTCAPACITY_EMPTY_ELEMENTDATA扩容只能是10.其他则不限制
         ensureCapacityInternal(int minCapacity) : 私有扩容方法.除了DEFAULTCAPACITY_EMPTY_ELEMENTDATA特殊.其他都是直接扩容.
         ensureExplicitCapacity(int minCapacity): 上面两个方法都调用该方法.确定最小容量大于当前容量后,即可扩容.
         grow(int minCapacity) : 真正实现扩容的方法. 取传入的最小容量 和 当前容量 + (当前容量 右移 1位)的最大值扩容.
            使用了Arrays.copyOf(elementData, newCapacity);方法实现数组复制和扩容.
         hugeCapacity(int minCapacity) : 防止容量超过最大值(如过超出了MAX_ARRAY_SIZE,直接扩容为Integer.MAX_VALUE),再超出后抛出异常
    >
    * 其他
    >
        每次从elementData中取出元素时,会向下转型(Object -> E)
        其底层操作elementData,大多通过System.arraycopy()方法
        clear()方法还是遍历elementData.循环赋值为null.不清楚为什么不直接将elementData == null.可能是为了GC效率?
        Itr:优化了AbstractList的迭代器.增加更细致的关于ConcurrentModificationException的判断
        ListItr:一样优化了下.
        
        以及一些关于java8的的方法.也就是Lamdba构建出对应的类,然后进行removeIf之类的操作.
    >
* Vector<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable
    * 综述
    >
        一个和List极其相似的集合.
        它的扩容grow(int minCapacity)方法.在capacityIncrement(每次扩容增加量)大于0时,为capacityIncrement,小于等于0时,为当前容量的两倍
        并且大部分方法都使用了synchronized关键字
    > 
    
* LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, java.io.Serializable
    * 综述
    >
        一个使用双链表的实现list和deque接口的集合.允许操作null元素
    >
    * 介绍
    >
        每个元素(节点)由静态内部类Node<E>构成,每个node包含  元素引用/上一node引用/下一node引用.如下:
            E item;
            Node<E> next;
            Node<E> prev;
        其中,首节点Node的prev为null,尾节点的prev为null.
        
        所以,遍历这个集合的方法如下. 从首节点开始,直到node.next为null.也就是尾节点.
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
         
        其中,如果要通过索引找到一个元素,需要如下,
        size>>1 的作用是判断要查找的索引是小于中间值还是大于中间值,据此判断是从头移动还是从尾移动.
                if (index < (size >> 1)) {
                    Node<E> x = first;
                    for (int i = 0; i < index; i++)
                        x = x.next;
                    return x;
                } else {
                    Node<E> x = last;
                    for (int i = size - 1; i > index; i--)
                        x = x.prev;
                    return x;
                }
        
        此外,它的迭代器原理类似,还有一个特殊的下行迭代器DescendingIterator,也就是在hasNext()方法中.使用hasPervious()
        
        它的toArray方法也是直接遍历,然后一次赋值到数组...        
    >
    * 属性
    >
        int size: 元素个数
        Node<E> first: 首节点
        Node<E> last: 尾节点
    >
    
#### Map相关
Key/Value映射对象,key不能包含重复的键,每个key指向一个value

* Map<K,V>: map接口,并不继承Collection<E>接口.map的最高级接口.
>
    特别的方法如下
    Set<K> keySet(): 返回key的set集合视图,也就是和SubList类似的维护了集合本身而已(SubList组合了创建它的list),所以map的修改会影响到这个set,反之亦然.
        如果map被修改,而set正在迭代(除非是迭代自己的remove操作),迭代的结果没有定义(????)
        该集合支持所有的删除功能.但不支持所有的增加功能.
    Collection<V> values(): 返回所有value的集合.同样的指向的是同一应用.其他同上.
    Set<Map.Entry<K, V>> entrySet(): 返回Entry<K,V>的set集合.其他同上
    
    default V getOrDefault(Object key, V defaultValue):key不存在时,返回默认值
    default V putIfAbsent(K key, V value) : key不存在时,才put
    default boolean remove(Object key, Object value) : 当key和value都为指定值时,才删除
    boolean replace(K key, V oldValue, V newValue) : 可以理解为CAS,当前值等于oldValue时,才替换
    还有一些java8相关的....暂时略过.
    
    内部接口
    Entry<K,V> : 一个key/value对.只能通过entrySet()方法获取.如果迭代时修改了map的值,则也是未定义(???).除非使用该接口内部修改的.
        可以获取key和value,修改value.其他还有些java8的.略过.
>
* AbstractMap<K,V> abstract  implements Map<K,V> : 抽象map类.最小化的实现了map接口
>
    基本操作的实现依靠的是 entrySet()返回的Set<Entry<K,V>>,然后调用.iterator()返回迭代器进行操作.例如如下get方法,key是要获取的元素的key
        Iterator<Entry<K,V>> i = entrySet().iterator();
        while (i.hasNext()) {
            Entry<K,V> e = i.next();
            if (key.equals(e.getKey()))
                return e.getValue();
        }
> 

    
#### Set相关-本来先看set...结果我忘了HashSet的实现是靠HashMap....
不包含重复元素的集合,因此最多存在一个null元素.

* Set<E> extends Collection<E> :  set接口,只是一个声明而已,没有任何实现,也没什么特别的方法

* AbstractSet<E> extends AbstractCollection<E> implements Set<E> : 抽象set类,也没什么特别的方法

* HashSet<E> extends AbstractSet<E>  implements Set<E>, Cloneable, java.io.Serializable : hashSet 保存了hashcode.无序,允许元素为null
    * 综述
    >
    
    >
    * 介绍
    >
    
    >
    * 属性
    >
    
    >





