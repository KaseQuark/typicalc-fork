with (import <nixpkgs> {});
let
  mavenJdk11 = maven.override {
    jdk = pkgs.jdk11;
  };
  my-python3-packages = python3-packages: with python3-packages; [
    lxml
  ];
  python3-with-my-packages = pkgs.python3.withPackages my-python3-packages;
in
mkShell {
  nativeBuildInputs = [
    file
    ripgrep
    mavenJdk11
    nodejs-14_x
    which # required by Vaadin
    python3-with-my-packages
  ];
}
