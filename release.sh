git checkout main
git pull --all

git checkout release
git pull --all

git merge origin/main
git push

git checkout main