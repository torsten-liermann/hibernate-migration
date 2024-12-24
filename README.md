# Hibernate Migration 5.3 to 6.2

This repository demonstrates the migration of a Hibernate project from version 5.3 to 6.2 using the OpenRewrite
framework. The migration process automates updates to APIs, annotations, and type handling to make the project
compatible with Hibernate 6.2.

## Prerequisites

- Java 17 or later

## Running the Migration

To perform the migration, execute the following Maven command:

```bash
mvnw clean install org.openrewrite.maven:rewrite-maven-plugin:5.47.0:run \
  -Drewrite.configLocation=https://raw.githubusercontent.com/torsten-liermann/openrewrite-pom-manipulation/refs/heads/main/rewrite.yml \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:LATEST,org.openrewrite.recipe:rewrite-hibernate:RELEASE \
  -Drewrite.activeRecipes=MigrateEAPDependencies,org.openrewrite.java.migrate.jakarta.JakartaEE10,org.openrewrite.hibernate.MigrateToHibernate62,org.openrewrite.hibernate.MigrateUserType,org.openrewrite.hibernate.TypeAnnotationParameter,org.openrewrite.hibernate.TypeDescriptorToType
```

### Parameters Explained

- **`rewrite.configLocation`**: Specifies the location of the `rewrite.yml` configuration file.
- **`rewrite.recipeArtifactCoordinates`**: Lists the artifact coordinates for OpenRewrite recipes used in the migration.
- **`rewrite.activeRecipes`**: Lists the active recipes applied during the migration.

### Recipes Used

1. **MigrateEAPDependencies**: Updates dependencies to match Jakarta EE 10.
2. **org.openrewrite.java.migrate.jakarta.JakartaEE10**: Migrates to Jakarta EE 10.
3. **org.openrewrite.hibernate.MigrateToHibernate62**: Applies changes for migrating to Hibernate 6.2.
4. **org.openrewrite.hibernate.MigrateUserType**: Updates the `UserType` interface for Hibernate 6.x compatibility.
5. **org.openrewrite.hibernate.TypeAnnotationParameter**: Adjusts `@Type` annotations for Hibernate 6.x.
6. **org.openrewrite.hibernate.TypeDescriptorToType**: Updates `JavaTypeDescriptor` and `SqlTypeDescriptor` to
   `JavaType` and `JdbcType`, respectively.

## Known Issue

During the migration, the following exception may occur:

```plaintext
[ERROR] Failed to execute goal org.openrewrite.maven:rewrite-maven-plugin:5.47.0:run (default-cli) on project hibernate-migration-demo:
Execution default-cli of goal org.openrewrite.maven:rewrite-maven-plugin:5.47.0:run failed:
Error while visiting src\main\java\org\examples\migration\hibernate\types\MyEnumerationPersistence.java:
java.lang.ClassCastException: class org.openrewrite.java.tree.J$Identifier cannot be cast to class org.openrewrite.java.tree.J$FieldAccess
(org.openrewrite.java.tree.J$Identifier and org.openrewrite.java.tree.J$FieldAccess are in unnamed module of loader org.codehaus.plexus.classworlds.realm.ClassRealm @71a2df1)

[ERROR]   org.openrewrite.hibernate.MigrateUserType$1$1.visitReturn(MigrateUserType.java:100)
[ERROR]   org.openrewrite.hibernate.MigrateUserType$1$1.visitReturn(MigrateUserType.java:91)
[ERROR]   org.openrewrite.java.tree.J$Return.acceptJava(J.java:4995)
[ERROR]   org.openrewrite.java.tree.J.accept(J.java:59)
[ERROR]   org.openrewrite.TreeVisitor.visit(TreeVisitor.java:250)
[ERROR]   org.openrewrite.TreeVisitor.visitAndCast(TreeVisitor.java:320)
[ERROR]   org.openrewrite.java.JavaVisitor.visitRightPadded(JavaVisitor.java:1367)
[ERROR]   org.openrewrite.java.JavaVisitor.lambda$visitBlock$4(JavaVisitor.java:397)
[ERROR]   org.openrewrite.internal.ListUtils.map(ListUtils.java:243)
[ERROR]   org.openrewrite.internal.ListUtils.map(ListUtils.java:265)
[ERROR]   org.openrewrite.java.JavaVisitor.visitBlock(JavaVisitor.java:396)
[ERROR]   org.openrewrite.java.JavaIsoVisitor.visitBlock(JavaIsoVisitor.java:88)
[ERROR]   org.openrewrite.java.JavaIsoVisitor.visitBlock(JavaIsoVisitor.java:30)
[ERROR]   org.openrewrite.java.tree.J$Block.acceptJava(J.java:838)
[ERROR]   org.openrewrite.java.tree.J.accept(J.java:59)
[ERROR]   org.openrewrite.TreeVisitor.visit(TreeVisitor.java:250)
```
