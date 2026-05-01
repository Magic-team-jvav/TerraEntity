package org.confluence.terraentity.utils;

/**
 * 循环数组缓存，使用固定大小的数组，存储指定数量的元素，当数组满时，将覆盖最旧的元素。
 */
public class CircularArrayBuffer<T> {
    private T[] positions;
    private int posPointer = -1;
    private int count = 0;

    public CircularArrayBuffer(T[] positions) {
        this.positions = positions;

    }

    public void add(T pos) {
        this.next();
        this.positions[this.posPointer] = pos;
        if (count < positions.length) {
            count++;
        }
    }

    /**
     * 指针移动到下一个位置
     */
    public void next() {
        this.posPointer = (this.posPointer + 1) % this.positions.length;
    }

    /**
     * 获取距离最新元素指定位置元素
     */
    public T get(int index) {
        if (index >= count) return null;
        return this.positions[(this.posPointer + this.positions.length - index) % this.positions.length];
    }

    /**
     * 获取最新的元素
     */
    public T get() {
        return this.positions[posPointer];
    }

    /**
     * 总容量
     */
    public int getCapability() {
        return this.positions.length;
    }

    /**
     * 实际存储的元素数量
     */
    public int size() {
        return this.count;
    }

    public boolean isFull() {
        return this.posPointer == this.positions.length - 1;
    }

    /**
     * 获取所有有效元素（按时间顺序，从旧到新）
     */
    private T[] getElementsInOrder() {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[this.count];
        for (int i = 0; i < this.count; i++) {
            result[i] = this.get(this.count - 1 - i);
        }
        return result;
    }

    /**
     * 原地扩容（会修改当前实例）
     * @param newCapacity 新容量
     */
    public void expand(int newCapacity) {
        if (newCapacity <= this.positions.length) {
            return; // 新容量不大于当前容量，不扩容
        }

        @SuppressWarnings("unchecked")
        T[] newPositions = (T[]) new Object[newCapacity];

        // 保存所有有效元素
        T[] validElements = getElementsInOrder();
        int validCount = validElements.length;

        // 复制到新数组
        System.arraycopy(validElements, 0, newPositions, 0, validCount);

        // 更新数组引用
        this.positions = newPositions;

        // 重置指针
        this.posPointer = validCount - 1;

    }
}
