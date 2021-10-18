package androidx.recyclerview.widget

abstract class ExtendedListAdapter<T, VH : RecyclerView.ViewHolder>(
    private val differ: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(differ) {

    override fun getCurrentList(): MutableList<T> {
        return mDiffer.currentList.toMutableList()
    }

}