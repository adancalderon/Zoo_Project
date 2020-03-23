
import java.util.*;


public class UnionFind<E> {                         //John's Union Find class

    public ArrayList<HashSet<E>> sets = new ArrayList<HashSet<E>>();

    public int union(int gr1, int gr2)
    {
        if ( gr1==gr2 ) return gr1;

        if ( sets.get(gr1).size()>sets.get(gr2).size() ) return join(gr2,gr1);
        else return join(gr1,gr2);
    }

    private int join(int from, int to)
    {
        sets.get(to).addAll( sets.get(from) );
        sets.get(from).clear();

        return to;
    }

    public int add(E e)
    {
        HashSet<E> set = new HashSet<E>();
        int set_num = sets.size();
        set.add(e);
        sets.add( set );

        return set_num;
    }

    public boolean find(E e1, E e2)
    {
        Integer in_set = find( e1 );

        return in_set==null ? false : sets.get(in_set).contains(e2);
    }

    public Integer find(E e1)
    {
        for( int i=0; i<sets.size(); i++ )
            if ( sets.get(i).contains(e1) ) return i;

        return null;
    }

    public HashSet get (int set_num){
        return sets.get(set_num);
    }

    public int count(int set)
    {
        return sets.get(set).size();
    }
    public void display(){
        System.out.println("SET# \t SET");
        for (int i = 0; i < sets.size(); i++) {
            System.out.println(i+"\t"+sets.get(i));
        }
    }


}