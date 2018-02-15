# Sectioned Recycler View

## Introduction
So your table has a header?

Avoid this:

    // onBindViewholder
    holder.item = items[position] - 1

    // getItemCount
    return items.count() + 1

Instead, use Sectioned Recycler View. It's a small wrapper around RecyclerViewAdapter which adds support for sections.

## Usage

Extend your adapter from SectionedRecyclerViewAdapter:

    class YourAdapter : SectionedRecyclerViewAdapter<YourViewHolder>()

Override all required properties and methods:

    override val numberOfSections: Int = 2

    override fun itemCount(section: Int): Int {
        return if (section == 0) {
            1
        } else {
            values.size
        }
    }

    override fun itemViewType(pos: SectionPosition): Int {
        // use pos.section and pos.position to get the section and relative position in the section
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: SectionPosition) {
        // use SectionPosition here as well
    }

## Full Table with Header Example

    class YourAdapter(var values: List<String>,
                      var listener: HomeFragment.OnListFragmentInteractionListener?) :
        SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>() {

        override val numberOfSections: Int = 2
        private val viewTypeHeader = 0
        private val viewTypeRow = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == viewTypeHeader) {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.header_cars, parent, false)
                HeaderViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.fragment_home_item, parent, false)
                RowViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: SectionPosition) {
            if (holder is RowViewHolder) {
                holder.text = values[pos.position]
            }
        }

        override fun itemCount(section: Int): Int {
            return if (section == 0) {
                1
            } else {
                values.size
            }
        }

        override fun itemViewType(pos: SectionPosition): Int {
            return if (pos.section == 0) {
                viewTypeHeader
            } else {
                viewTypeRow
            }
        }

        inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)
        inner class RowViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
            private val titleView: TextView = view.findViewById(R.id.title)

            var text: String? = null
                set(value) {
                    field = value
                    if (value != null) {
                        titleView.text = value
                    }
                }
        }
    }
