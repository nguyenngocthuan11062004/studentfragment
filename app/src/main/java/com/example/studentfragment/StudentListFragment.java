package com.example.studentfragment;



import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studentmanagerfragment.R
import com.example.studentmanagerfragment.adapters.StudentListAdapter
import com.example.studentmanagerfragment.StudentViewModel


class StudentListFragment : Fragment() {
    private lateinit var adapter: StudentListAdapter
    private val studentViewModel: StudentViewModel by activityViewModels()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
            setHasOptionsMenu(true)
            val view = inflater.inflate(R.layout.fragment_student_list, container, false)
            val listView = view.findViewById<ListView>(R.id.list_view_students)

            adapter = StudentListAdapter(requireContext(), studentViewModel.students.value ?: mutableListOf())
    listView.adapter = adapter

    registerForContextMenu(listView)

    studentViewModel.students.observe(viewLifecycleOwner) { updatedList ->
            adapter.updateData(updatedList)
    }
    listView.onItemClickListener = null
    return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_new -> {
                val action = StudentListFragmentDirections.actionStudentListToAddEditStudent(
                        studentName = null,
                        studentId = null,
                        position = -1
                )
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
            menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val selectedStudent = studentViewModel.students.value?.get(info.position)

        return when (item.itemId) {
            R.id.context_edit -> {
                if (selectedStudent != null) {
                    val action = StudentListFragmentDirections.actionStudentListToAddEditStudent(
                            studentName = selectedStudent.studentName,
                            studentId = selectedStudent.studentId,
                            position = info.position
                    )
                    findNavController().navigate(action)
                }
                true
            }
            R.id.context_remove -> {
                studentViewModel.students.value?.let {
                    val updatedList = it.toMutableList()
                    updatedList.removeAt(info.position)
                    studentViewModel.students.value = updatedList
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}