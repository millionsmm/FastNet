package vpour.fastnet;

/**
 *
 */

public class SortHelper {
    //冒泡
    public static void bubleSort(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {//循环n-1次，每循环完一次，冒泡得一个最大值
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }

            }
        }
    }

    //合并排序
    public static void mergeSort(Comparable a[], int left, int right) {
        Comparable b[] = new Comparable[a.length];
        if (left < right) {
            int i = (left + right) / 2;
            mergeSort(a, left, i);
            mergeSort(a, i + 1, right);
            merge(a, b, left, i, right);//合并到数组b
            copy(a, b, left, right);//复制回数组a
        }
    }

    private static void copy(Comparable[] a, Comparable[] b, int left, int right) {
    }

    //合并c[l:m]和c[m+1:r]到d[l:r]
    public static void merge(Comparable c[], Comparable d[], int l, int m, int r) {
        int i = 1, j = m + 1, k = l;
        while ((i <= m) && (j <= r)) {
            if (c[i].compareTo(c[j]) <= 0) {
                d[k++] = c[i++];
            } else {
                d[k++] = c[j++];
            }
        }
        if (i > m) {
            for (int q = j; q <= r; q++) {
                d[k++] = c[q];
            }
        } else {
            for (int q = i; q <= m; q++) {
                d[k++] = c[q];
            }
        }

    }

    /**
     * private int partition(Comparable a[],int p,int r){
     * int i=p,j=r+1;
     * Comparable x=a[p];
     * while(true){
     * while(a[++i].compareTo(x)<0&&i<r) ;//小于x的元素
     * while(a[--j].compareTo(x)>0);//大于x的元素
     * if(i>=j) break;
     * <p>
     * }
     * <p>
     * }
     */
    //快速排序
    public static void quickSort(int a[], int p, int r) {
        if (p < r) {
            int q = partition(a, p, r);
            quickSort(a, p, q - 1);
            quickSort(a, q + 1, r);
        }
    }

    public static int partition(int a[], int p, int r) {
        int i = p, j = r;
        int x = a[p];
        while (i < j) {
            //从右往左找比x小的数，填充a[i]
            while (i < j && a[j] >= x) j--;
            if (i < j) {
                a[i] = a[j];
                i++;
            }

            //从左往右找比x大的数，填充a[j]
            while (i < j && a[i] < x) i++;
            if (i < j) {
                a[j] = a[i];
                j--;
            }
        }
        a[i] = x;
        return i;

    }

    //直接插入排序
    public static void insertSort(int a[], int n) {
        int j, k;
        for (int i = 1; i < n; i++) {
            for (j = i - 1; j >= 0; j--) {
                if (a[j] < a[i]) {
                    break;
                }
            }
            if (j != i - 1) {
                int temp = a[i];
                for (k = i - 1; k < j; k--) {
                    a[k + 1] = a[k];
                }
                a[k + 1] = temp;
            }
        }
    }

    //希尔排序
    public void shellSort(int a[], int n) {
        int i, j, gap;
        for (gap = n / 2; gap > 0; gap = gap / 2) {
            for (i = 0; i < gap; i++) {
                for (j = i + gap; j < n; j = j + gap) {
                    if (a[j] < a[j - gap]) {
                        int temp = a[j];
                        int k = j - gap;
                        while (k >= 0 && a[k] > temp) {
                            a[k + gap] = a[k];
                            k = k - gap;//使k<0跳出while循环
                        }
                        a[k + gap] = temp;
                    }
                }
            }
        }
    }

    //直接选择排序
    public void selectSort(int a[], int n) {
        int i, j, min;
        for (i = 0; i < n; i++) {
            min = i;
            for (j = i + 1; j < n; j++) {
                if (a[j] < a[min]) min = j;
            }
            int temp = a[min];
            a[min] = a[i];
            a[i] = temp;

        }
    }

    //堆排序
    public void heapSort(int a[]) {
        //将无序数组构成一个大顶堆
        for (int i = a.length / 2; i >= 0; i--) {
            heapAdjust(a, i, a.length);
        }
        //逐步将每个最大值的根节点与末尾元素交换，并且再调整二叉树，使其成为大顶堆
        for (int i = a.length - 1; i > 0; i--) {
            swap(a, 0, i);//将堆顶记录和当前未经排序子序列的最后一个记录交换;
            heapAdjust(a, 0, i);//交换之后，需要重新检查堆是否符合大顶堆，不符合则要调整
        }
    }

    /**
     * 构建堆的过程
     *
     * @param a 需要排序的数组
     * @param i 需要构建堆的根节点的序号
     * @param n 数组的长度
     */
    public void heapAdjust(int[] a, int i, int n) {
        int child, father;
        for (father = a[i]; leftChild(i) < n; i = child) {
            child = leftChild(i);
            //如果左子树小于右子数，则需要比较右子树和父节点
            if (child != n - 1 && a[child] < a[child + 1]) {
                child++;//序号增1，指向右子树
            }
            //如果父结点小于孩子结点，则需要交换
            if (father < a[child]) {
                a[i] = a[child];
            } else {
                break;//不需要调整
            }
        }
        a[i] = father;
    }

    //获取到左子结点
    public int leftChild(int i) {
        return 2 * i + 1;
    }

    //交换元素位置
    private void swap(int a[], int index1, int index2) {
        int temp = a[index1];
        a[index1] = a[index2];
        a[index2] = temp;
    }


}
