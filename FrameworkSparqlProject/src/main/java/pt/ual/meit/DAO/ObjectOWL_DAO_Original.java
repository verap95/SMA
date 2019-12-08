package pt.ual.meit.DAO;

// @Component
// public class ObjectOWL_DAO_Original implements ObjectOWL {
//
// private List<Classe> classList = new ArrayList<>();
// private List<Class_> classList_ = new ArrayList<>();
//
// @Override
// public List<Classe> getAllClasses() {
// return classList;
// }
//
// @Override
// public void setClasses(String name) {
// Classe classe = new Classe();
// classe.setName(name);
// classList.add(classe);
//
// Class_ class_ = new Class_();
// class_.setName(name);
// classList_.add(class_);
// }
//
// @Override
// public void addDtPropertytoClass_(String nameClass, Property property) {
// for (Class_ c : classList_) {
// if (c.getName().equals(nameClass)) {
// c.getListDataTypeProperty().add(property);
// }
// }
// }
//
// @Override
// public void addObjPropertytoClass_(String nameClass, Property property) {
// for (Class_ c : classList_) {
// if (c.getName().equals(nameClass)) {
// c.getListObjectProperty().add(property);
// }
// }
//
// }
//
// @Override
// public void addPropertytoClass(String nameClass, Property property) {
// for (Classe c : classList) {
// if (c.getName().equals(nameClass)) {
// c.getPropertiesList().add(property);
// }
// }
// }
//
// @Override
// public List<Class_> getAllClass_() {
// return classList_;
// }
// }
