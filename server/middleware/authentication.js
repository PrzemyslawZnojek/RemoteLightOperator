const fb = require('../firebase');

function authentication(req, res, next) {
  const curentUser = fb.getCurrentUser();

  if (!curentUser) {
    return res.redirect('/login');
  }

  next();
}

module.exports = authentication;