package hgh.project.baemin_clone.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import hgh.project.baemin_clone.model.CellType
import hgh.project.baemin_clone.model.Model
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.mapper.ModelViewHolderMapper
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.viewholder.ModelVIewHolder

class ModelRecyclerAdapter<M : Model, VM : BaseViewModel>(
    private var modelList: List<Model>,
    private val viewModel: VM,
    private var resourceProvider: ResourceProvider,
    private val adapterListener: AdapterListener
) : ListAdapter<Model, ModelVIewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun getItemCount(): Int = modelList.size

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelVIewHolder<M> {
        return ModelViewHolderMapper.map(
            parent,
            CellType.values()[viewType],
            viewModel,
            resourceProvider
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelVIewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: MutableList<Model>?) {
        list?.let {
            modelList =it }
        super.submitList(list)
    }




}