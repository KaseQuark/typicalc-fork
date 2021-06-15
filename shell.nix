with (import <nixpkgs> {});
let mavenJdk11 = maven.override {
  jdk = pkgs.jdk11;
};
in
mkShell {
  nativeBuildInputs = [
    file
    ripgrep
    mavenJdk11
    python3
  ];
}
