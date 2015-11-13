
/**
 * Created by DongHyun on 2015-11-11.
 */
public class HashMap<V,K> {
    private final int DEFAULT_TABLE_SIZE = 128;
    private float threshold = 0.75f;
    private int maxSize = 96;
    private int size = 0;

    HashEntry[] table;

    public HashMap() {
        table = new HashEntry[DEFAULT_TABLE_SIZE];
        for (int i = 0; i < DEFAULT_TABLE_SIZE; i++)
            table[i] = null;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
        maxSize = (int) (table.length * threshold);
    }

    public V get(K key) {
        int hash = getIndex(key);
        int initialHash = -1;

        //테이블전체가 꽉차있지않고 && 해당노드가 지워지거나 다른것이있을때 반복
        while (hash != initialHash && (table[hash] == DeletedEntry.getUniqueDeletedEntry() || (table[hash] != null && !table[hash].getKey().equals(key)))) {
            if (initialHash == -1)
                initialHash = hash;
            hash = (hash + 1) % table.length;
        }
        //못찾은 경우
        if (table[hash] == null || hash == initialHash) {
            return null;
        } else {// 찾은경우
            return (V) table[hash].getValue();
        }
    }

    public void put(K key, V value) {
        int hash = getIndex(key);
        int initialHash = -1;
        int indexOfDeletedEntry = -1;

        //테이블전체가 꽉차있지않고 && 해당노드가 지워지거나 다른것이있을때 반복
        while (hash != initialHash && (table[hash] == DeletedEntry.getUniqueDeletedEntry() || (table[hash] != null && !table[hash].getKey().equals(key)))) {
            if (initialHash == -1)
                initialHash = hash;
            if (table[hash] == DeletedEntry.getUniqueDeletedEntry())
                indexOfDeletedEntry = hash;
            hash = (hash + 1) % table.length;
        }


        // 꽉차있거나 빈노드가 있어서 while 루프를 멈췄지만 삭제된 노드가 있는경우
        if (indexOfDeletedEntry != -1 && (table[hash] == null || hash == initialHash) ) {
            table[indexOfDeletedEntry] = new HashEntry(key, value); // 삭제된 곳에 삽입
            size++;
        }


        // 꽉차있지 않은경우
        else if (initialHash != hash) {
            // value값을 수정하는경우
            if (table[hash] != DeletedEntry.getUniqueDeletedEntry() && table[hash] != null && table[hash].getKey().equals(key)) {
                table[hash].setValue(value);
            }
            else {
                table[hash] = new HashEntry(key, value);
                size++;
            }

            if(size>=maxSize) //최대적재율보다 커진경우 resize();
                resize();
        }
    }

    public void remove(K key) {
        int hash = getIndex(key);
        int initialHash = -1;
        //테이블전체가 꽉차있지않고 && 해당노드가 지워지거나 다른것이있을때 반복
        while (hash != initialHash && (table[hash] == DeletedEntry.getUniqueDeletedEntry() || table[hash] != null && !table[hash].getKey().equals(key))) {
            if (initialHash == -1)
                initialHash = hash;
            hash = (hash + 1) % table.length;
        }

        //전체가 꽉차있지않고 && 해당노드가 null이아닌경우 삭제
        if (hash != initialHash && table[hash] != null) {
            table[hash] = DeletedEntry.getUniqueDeletedEntry();
            size--;
        }
    }
    private int getIndex(K key) {
        //Digit Folding method

        int sum = 0;

        String strKey = key.toString();

        for(int i=0;i<strKey.length();i++){
            Character ch = strKey.charAt(i);
            sum += Character.getNumericValue(ch);
        }


        int hash = sum % table.length;
        return hash;
    }



    private void resize() {
        int tableSize = 2 * table.length;
        maxSize = (int) (tableSize * threshold);

        HashEntry[] oldTable = table;
        table = new HashEntry[tableSize];
        size = 0;

        //그대로 복사X 다시 해싱 후 put
        for (int i = 0; i < oldTable.length; i++)
            if (oldTable[i] != null && oldTable[i] != DeletedEntry.getUniqueDeletedEntry())
                put((K)oldTable[i].getKey(), (V)oldTable[i].getValue());
    }


    public static void main(String[] args){
        HashMap hashMap = new HashMap();

        for(int i=1;i<130;i++){
            hashMap.put("abcd"+i,i);
        }

        for(int i=1;i<130;i++){
            System.out.println(hashMap.get("abcd"+i));
        }
        hashMap.put("Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        hashMap.put("In eget lacus rhoncus", "In eget lacus rhoncus, facilisis justo ac, venenatis turpis.");
        hashMap.put("Vestibulum aliquet", "Vestibulum aliquet leo sed tellus faucibus, quis feugiat felis lobortis.");
        hashMap.put("Nunc ut augue sit", "Nunc ut augue sit amet leo consectetur volutpat.");
        hashMap.put("Praesent fermentum", "Praesent fermentum ex quis nunc porta, sit amet ultricies justo ultricies.");

        hashMap.put("Morbi vehicula justo", "Morbi vehicula justo aliquam velit lacinia tristique.");

        hashMap.put("Suspendisse varius", "Suspendisse varius orci ullamcorper, porta tellus sed, dignissim diam.");
        hashMap.put("Nunc fermentum arcu", "Nunc fermentum arcu viverra, porta nibh eget, luctus quam.");
        hashMap.put("Nam finibus felis non", "Nam finibus felis non magna scelerisque, eget fringilla nulla scelerisque.");
        hashMap.put("Donec sagittis eros", "Donec sagittis eros quis dui auctor porta.");


        System.out.println(hashMap.get("Lorem ipsum dolor sit amet"));


        hashMap.remove("Morbi vehicula justo");

        System.out.println(hashMap.get("Morbi vehicula justo"));

    }
}